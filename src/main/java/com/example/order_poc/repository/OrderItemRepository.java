package com.example.order_poc.repository;

import com.example.order_poc.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository layer: order items; unused, items save via cascade.
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
