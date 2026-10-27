package com.example.order_poc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO: product JSON returned to the client.
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, needed by Jackson for JSON.
@NoArgsConstructor
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
}
