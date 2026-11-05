package com.example.order_poc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO: JSON body for creating an account.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "name is required")
    @Size(max = 255, message = "name must be at most 255 characters")
    private String name;

    @NotBlank(message = "email is required")
    // Rejects text that is not a valid email address.
    @Email(message = "email must be a valid email address")
    private String email;

    @NotBlank(message = "password is required")
    // BCrypt only uses the first 72 bytes.
    @Size(min = 8, max = 72, message = "password must be 8 to 72 characters")
    private String password;
}
