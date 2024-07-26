package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // root that should return 200 if the API is up and available
                        .requestMatchers("/").permitAll()
                        // root for user login (take username or email and password)
                        .requestMatchers("/login").permitAll()
                        // root for user logout (take no arg) -> force the token expiration
                        .requestMatchers("/logout").permitAll()
                        // root for user sign up (take unique username, email and password)
                        .requestMatchers("/signup").permitAll()
                        // root that return dashboard datas (all task of the user)
                        .requestMatchers("/todolist/**").hasRole("USER")
                        // root get and post that return or modify an user task by id (is_checked, libelle, doneBefore)
                        .requestMatchers("/tasks/**").hasRole("USER")
                        // root get and post that return or modify the user datas (username, email and password)
                        .requestMatchers("/profil").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers( "/h2-console/**");
    }
}