package com.example.order_poc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// DTO: order JSON returned to the client, flattened.
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, needed by Jackson for JSON.
@NoArgsConstructor
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String userName;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
