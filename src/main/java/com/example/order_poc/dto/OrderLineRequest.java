package com.example.order_poc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO: one requested line, a product id and quantity.
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, needed by Jackson for JSON.
@NoArgsConstructor
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class OrderLineRequest {

    // Field description and example value shown in Swagger UI.
    @Schema(description = "Id of the product to order", example = "1")
    // Rejects a missing value.
    @NotNull(message = "productId is required")
    private Long productId;

    @Schema(description = "How many units, at least 1", example = "2")
    // Integer, not int: missing value becomes null, not error.
    @NotNull(message = "quantity is required")
    // Rejects values below 1.
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;
}
