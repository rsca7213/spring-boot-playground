package com.playground.api.controllers;

import com.playground.api.dtos.auth.LoginUserBody;
import com.playground.api.dtos.auth.LoginUserResponse;
import com.playground.api.dtos.auth.RegisterUserBody;
import com.playground.api.dtos.auth.RegisterUserResponse;
import com.playground.api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided details. Returns the created user's information."
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(requestBody));
    }

    @Operation(
            summary = "Login a user",
            description = "Logs in a user with the provided credentials. Returns a JWT token for authentication."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@Valid @RequestBody LoginUserBody requestBody) {
        return ResponseEntity.ok(authService.loginUser(requestBody));
    }
}
