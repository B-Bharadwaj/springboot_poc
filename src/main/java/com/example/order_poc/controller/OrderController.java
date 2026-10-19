package com.example.order_poc.controller;

import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/slow")
    public ResponseEntity<List<OrderResponse>> getAllSlow() {
        return ResponseEntity.ok(orderService.getAllOrdersSlow());
    }

    @GetMapping("/fast")
    public ResponseEntity<List<OrderResponse>> getAllFast() {
        return ResponseEntity.ok(orderService.getAllOrdersFast());
    }
}
