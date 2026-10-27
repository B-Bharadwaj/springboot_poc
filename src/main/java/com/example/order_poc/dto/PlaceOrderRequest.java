package com.example.order_poc.dto;

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

    // Rejects a missing value.
    @NotNull(message = "userId is required")
    private Long userId;

    // Rejects a missing or empty list.
    @NotEmpty(message = "items must contain at least one line")
    // Also validates every OrderLineRequest inside the list.
    @Valid
    private List<OrderLineRequest> items;
}
