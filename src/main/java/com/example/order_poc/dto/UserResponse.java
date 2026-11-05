package com.example.order_poc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// DTO: user data sent to clients; never the password hash.
@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
}
