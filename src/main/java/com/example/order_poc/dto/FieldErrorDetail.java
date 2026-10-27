package com.example.order_poc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// DTO: one invalid field and why it was rejected.
// Lombok: generates getters.
@Getter
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class FieldErrorDetail {

    private String field;
    private String message;
}
