package com.playground.api.controllers;

import com.playground.api.dtos.auth.*;
import com.playground.api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(requestBody));
    }

    @Operation(
            summary = "Login a user",
            description = "Logs in a user with the provided credentials. Returns a JWT token for authentication."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@Valid @RequestBody LoginUserBody requestBody) {
        String token = authService.loginUser(requestBody);

        // set an HTTP-only cookie
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(
                        "Set-Cookie",
                        "auth=" + token + "; HttpOnly; Path=/; SameSite=Strict"
                )
                .body(LoginUserResponse.builder().success(true).build());
    }


    @Operation(
            summary = "Get current authenticated user",
            description = "Returns the details of the currently authenticated user."
    )
    @GetMapping("/current-user")
    public ResponseEntity<GetCurrentUserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authService.getCurrentUser(authentication));
    }
}
