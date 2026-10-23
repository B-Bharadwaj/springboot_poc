package com.example.order_poc.mapper;

import com.example.order_poc.dto.OrderItemResponse;
import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.model.Order;
import com.example.order_poc.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

// Mapper layer: converts an Order entity into an OrderResponse.
// Spring creates and manages this class as a bean.
@Component
public class OrderMapper {

    // Touching items or user may run lazy SELECTs (N+1).
    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(order.getId(), order.getUser().getName(), order.getStatus(),
                order.getCreatedAt(), items);
    }

    // getProduct() may run a lazy SELECT for the product.
    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(item.getProduct().getName(), item.getQuantity(), item.getUnitPrice());
    }
}
