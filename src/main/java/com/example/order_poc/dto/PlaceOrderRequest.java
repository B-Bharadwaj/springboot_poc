package com.example.order_poc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// DTO: JSON body for placing an order.
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, needed by Jackson for JSON.
@NoArgsConstructor
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class PlaceOrderRequest {

    // Field description and example value shown in Swagger UI.
    @Schema(description = "Id of the user placing the order", example = "1")
    // Rejects a missing value.
    @NotNull(message = "userId is required")
    private Long userId;

    @Schema(description = "Order lines, at least one",
            example = "[{\"productId\": 1, \"quantity\": 2}]")
    // Rejects a missing or empty list.
    @NotEmpty(message = "items must contain at least one line")
    // @Valid on the type argument validates every list element.
    private List<@Valid OrderLineRequest> items;
}
