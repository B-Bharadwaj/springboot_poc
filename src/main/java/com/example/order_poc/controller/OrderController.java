package com.example.order_poc.controller;

import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.dto.PageResponse;
import com.example.order_poc.dto.PlaceOrderRequest;
import com.example.order_poc.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    @GetMapping("/slow")
    public ResponseEntity<List<OrderResponse>> getAllSlow() {
        return ResponseEntity.ok(orderService.getAllOrdersSlow());
    }

    @GetMapping("/fast")
    public ResponseEntity<List<OrderResponse>> getAllFast() {
        return ResponseEntity.ok(orderService.getAllOrdersFast());
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<OrderResponse>> getPage(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersPage(pageable));
    }
}
