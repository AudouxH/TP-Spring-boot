package com.example.demo.controllers;

import com.example.demo.Utils.JwtUtil;
import com.example.demo.models.JwtResponse;
import com.example.demo.models.User;
import com.example.demo.models.UserResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.services.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Signup root which register a new user
    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody User user) {

        if (user.getUsername() == null || user.getUsername().isEmpty() || userDetailsService.usernameExists(user.getUsername())) {
            return ResponseEntity.badRequest().body(new JwtResponse("Invalid username"));
        } else if (user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new JwtResponse("Invalid email"));
        } else if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body(new JwtResponse("Invalid password"));
        } else {
            // register new user
            User savedUser = userDetailsService.save(user);
            return ResponseEntity.ok(new JwtResponse("User registered successfully at " + savedUser.getUsername() + " with pass: " + savedUser.getPassword() + " and with id: " + savedUser.getId()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(new JwtResponse("Invalid Username or password"));
        } else {
            try {
                // try to get auth token using username and password passed inside the request
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
                );
                // get userDetails from the auth (username, password, getAuthorities, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled)
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                if (userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired() && userDetails.isEnabled()) {
                    // get new auth token using the username and his authorities
                    String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities());

                    // return the generated auth token
                    return ResponseEntity.ok(new JwtResponse(token));
                } else {
                    return ResponseEntity.badRequest().body(new JwtResponse("Account as expired or is lock"));
                }
            } catch (AuthenticationException e) {
                return ResponseEntity.badRequest().body(new JwtResponse("Cannot create new session token"));
            }
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(token);

            Optional<User> optionalUser = userDetailsService.findByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setTokenInvalidatedAt(LocalDateTime.now());
                userDetailsService.save(user);
                return ResponseEntity.ok(new JwtResponse("Successfully logged out"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during logout process");
        }
    }

    @GetMapping("/profil")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // get user info from an auth token
        try {
            // get token from the authorizationHeader by replacing "Bearer " by ""
            String token = authorizationHeader.replace("Bearer ", "");
            // If token is validate (taking the token and the extracted username from the token
            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                // get claims which are token data
                Claims claims = jwtUtil.extractClaims(token);
                // get username from token data
                String username = claims.getSubject();

                // get now user datas from his unique username
                Optional<User> optionalUser = userDetailsService.findByUsername(username);

                // if user is found
                if (optionalUser.isPresent()) {
                    // set user inside no option user
                    User user = optionalUser.get();

                    LocalDateTime tokenIssuedAt = jwtUtil.extractIssuedAt(token);

                    if (user.getTokenInvalidatedAt() != null && tokenIssuedAt.isBefore(user.getTokenInvalidatedAt())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token has been invalidate");
                    }

                    if (jwtUtil.validateToken(token, username)) {
                        // return user datas (username, email, role)
                        return ResponseEntity.ok(new UserResponse(user.getUsername(), user.getEmail(), user.getRole()));
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error processing token");
        }
    }
}
