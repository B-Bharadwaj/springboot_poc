package com.example.order_poc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// DTO: login result; client sends token as "Bearer <token>".
@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type;
    private long expiresInMinutes;
}
