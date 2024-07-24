package com.example.demo.controllers;

import com.example.demo.Utils.JwtUtil;
import com.example.demo.models.JwtResponse;
import com.example.demo.models.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.services.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody User user) {

        if (user.getUsername() == null && user.getPassword() == null) {
            return ResponseEntity.badRequest().body(new JwtResponse("Username and password and required"));
        }

        User savedUser = userDetailsService.save(user);
        return ResponseEntity.ok(new JwtResponse("User registered successfully at " + savedUser.getUsername() + " with pass: " + savedUser.getPassword() + " and with id: " + savedUser.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(new JwtResponse("Username and password are required"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities());

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new JwtResponse("Invalid username or password"));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                Claims claims = jwtUtil.extractClaims(token);
                String username = claims.getSubject();

                return ResponseEntity.ok(new JwtResponse(username));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error processing token");
        }
    }
}
