package com.example.order_poc.service;

import com.example.order_poc.dto.OrderLineRequest;
import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.dto.PageResponse;
import com.example.order_poc.dto.PlaceOrderRequest;
import com.example.order_poc.exception.InsufficientStockException;
import com.example.order_poc.exception.ResourceNotFoundException;
import com.example.order_poc.mapper.OrderMapper;
import com.example.order_poc.model.Order;
import com.example.order_poc.model.OrderItem;
import com.example.order_poc.model.Product;
import com.example.order_poc.model.User;
import com.example.order_poc.repository.OrderRepository;
import com.example.order_poc.repository.ProductRepository;
import com.example.order_poc.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersPage(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        List<OrderResponse> content = page.getContent().stream()
                .map(orderMapper::toResponse)
                .toList();
        return new PageResponse<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    // @Transactional (read-write) runs the whole method in one database transaction:
    // if any exception is thrown, every change made so far (stock updates, the order) is rolled back.
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("NEW");
        order.setCreatedAt(LocalDateTime.now());

        for (OrderLineRequest line : request.getItems()) {
            Product product = productRepository.findById(line.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + line.getProductId()));

            if (product.getQuantity() < line.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product '" + product.getName()
                        + "' (id " + product.getId() + "): requested " + line.getQuantity()
                        + ", available " + product.getQuantity());
            }
            product.setQuantity(product.getQuantity() - line.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(line.getQuantity());
            item.setUnitPrice(product.getPrice());
            order.getItems().add(item);
        }

        return orderMapper.toResponse(orderRepository.save(order));
    }
}
