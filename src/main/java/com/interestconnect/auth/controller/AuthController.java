package com.interestconnect.auth.controller;

import com.interestconnect.auth.dto.LoginRequest;
import com.interestconnect.auth.dto.LoginResponse;
import com.interestconnect.auth.dto.RegisterRequest;
import com.interestconnect.auth.dto.RegisterResponse;
import com.interestconnect.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.interestconnect.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

}