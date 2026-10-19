package com.example.order_poc.mapper;

import com.example.order_poc.dto.OrderItemResponse;
import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.model.Order;
import com.example.order_poc.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(order.getId(), order.getUser().getName(), order.getStatus(),
                order.getCreatedAt(), items);
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(item.getProduct().getName(), item.getQuantity(), item.getUnitPrice());
    }
}
