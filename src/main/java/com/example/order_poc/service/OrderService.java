package com.example.order_poc.service;

import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.mapper.OrderMapper;
import com.example.order_poc.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    // @Transactional keeps one database session open for the whole method (so lazy loading works);
    // readOnly = true tells Hibernate nothing will be written, so it can skip change tracking.
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersSlow() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersFast() {
        return orderRepository.findAllWithItems().stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}
