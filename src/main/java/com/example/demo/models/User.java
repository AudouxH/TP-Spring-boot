package com.example.demo.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "app_user")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String email;
    private String password;
    private String role = "USER";
    private LocalDateTime tokenInvalidatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public LocalDateTime getTokenInvalidatedAt() {return tokenInvalidatedAt;}

    public void setTokenInvalidatedAt(LocalDateTime tokenInvalidateAt) {this.tokenInvalidatedAt = tokenInvalidateAt;}
}
