package com.example.order_poc.controller;

import com.example.order_poc.dto.AuthResponse;
import com.example.order_poc.dto.ErrorResponse;
import com.example.order_poc.dto.LoginRequest;
import com.example.order_poc.dto.RegisterRequest;
import com.example.order_poc.dto.UserResponse;
import com.example.order_poc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller layer: public register and login endpoints.
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Register and log in")
public class AuthController {

    private final AuthService authService;

    // Spring passes in the AuthService (constructor injection).
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 201 with the new user; 400 invalid, 409 email taken.
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed or malformed JSON",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Email already registered",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    // 200 with a token; 401 on wrong email or password.
    @Operation(summary = "Log in and get a JWT")
    @ApiResponse(responseCode = "200", description = "Logged in",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed or malformed JSON",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid email or password",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
