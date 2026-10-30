package com.example.order_poc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO: JSON the client sends to create/update a product.
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, needed by Jackson for JSON.
@NoArgsConstructor
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class ProductRequest {

    // Field description and example value shown in Swagger UI.
    @Schema(description = "Product name", example = "Laptop")
    // Rejects null, empty or whitespace-only text.
    @NotBlank(message = "name is required")
    // Rejects text longer than max characters.
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @Schema(description = "Unit price, greater than 0", example = "999.99")
    // Double, not double: missing value becomes null, not error.
    @NotNull(message = "price is required")
    // Must be greater than zero.
    @Positive(message = "price must be greater than 0")
    private Double price;

    @Schema(description = "Units in stock, 0 or more", example = "10")
    @NotNull(message = "quantity is required")
    // Zero or more; zero means out of stock.
    @PositiveOrZero(message = "quantity must be 0 or more")
    private Integer quantity;

    @Schema(description = "Optional product description", example = "14-inch laptop, 16 GB RAM")
    // Optional, but at most 500 characters (matches the column).
    @Size(max = 500, message = "description must be at most 500 characters")
    private String description;
}
