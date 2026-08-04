package com.interestconnect.auth.controller;

import com.interestconnect.auth.dto.LoginRequest;
import com.interestconnect.auth.dto.LoginResponse;
import com.interestconnect.auth.dto.RegisterRequest;
import com.interestconnect.auth.dto.RegisterResponse;
import com.interestconnect.auth.service.AuthService;
import com.interestconnect.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication",
        description = "APIs for user registration, authentication and user profile"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register User",
            description = "Registers a new user in Interest Connect."
    )
    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @Operation(
            summary = "Login User",
            description = "Authenticates the user and returns a JWT access token."
    )
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }

    @Operation(
            summary = "Get Current User",
            description = "Returns the profile of the currently authenticated user."
    )
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {

        return ResponseEntity.ok(authService.getCurrentUser());
    }
}