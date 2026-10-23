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

// Service layer: order logic; calls repositories and the mapper.
// Spring creates and manages this class as a bean.
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    // Spring passes in repositories and mapper (constructor injection).
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    // One transaction keeps the session open for lazy loading.
    // readOnly: nothing is written, so Hibernate skips change tracking.
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersSlow() {
        // SELECT orders only; the mapper triggers extra SELECTs (N+1).
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    // Read-only transaction.
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersFast() {
        // One SELECT with joins loads everything at once.
        return orderRepository.findAllWithItems().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    // Read-only transaction.
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersPage(Pageable pageable) {
        // Page SELECT with OFFSET/LIMIT, plus a COUNT query.
        Page<Order> page = orderRepository.findAll(pageable);
        List<OrderResponse> content = page.getContent().stream()
                .map(orderMapper::toResponse)
                .toList();
        return new PageResponse<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    // Read-write transaction: any exception rolls back all changes.
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        // SELECT user; lambda throws 404 if not found.
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("NEW");
        order.setCreatedAt(LocalDateTime.now());

        for (OrderLineRequest line : request.getItems()) {
            // SELECT product; lambda throws 404 if not found.
            Product product = productRepository.findById(line.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + line.getProductId()));

            if (product.getQuantity() < line.getQuantity()) {
                // 409 Conflict; rollback undoes earlier stock changes too.
                throw new InsufficientStockException("Insufficient stock for product '" + product.getName()
                        + "' (id " + product.getId() + "): requested " + line.getQuantity()
                        + ", available " + product.getQuantity());
            }
            product.setQuantity(product.getQuantity() - line.getQuantity());
            // UPDATE stock; dirty checking would also save it.
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(line.getQuantity());
            // Keep the price at order time.
            item.setUnitPrice(product.getPrice());
            order.getItems().add(item);
        }

        // INSERT order; cascade inserts items; commits on return.
        return orderMapper.toResponse(orderRepository.save(order));
    }
}
