package com.example.order_poc.controller;

import com.example.order_poc.dto.OrderResponse;
import com.example.order_poc.dto.PageResponse;
import com.example.order_poc.dto.PlaceOrderRequest;
import com.example.order_poc.service.OrderService;
import com.example.order_poc.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller layer: order endpoints; calls the order service.
// Handles HTTP; return values become the response body.
@RestController
// All endpoints here start with /api/orders.
@RequestMapping("/api/orders")
// Swagger UI groups these endpoints under "Orders".
@Tag(name = "Orders", description = "Place and list orders")
public class OrderController {

    private final OrderService orderService;

    // Spring passes in the OrderService (constructor injection).
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Maps POST /api/orders to this method.
    // @RequestBody turns the JSON body into a PlaceOrderRequest.
    // @Valid checks the DTO's rules first; failure returns 400.
    // One-line description shown next to the endpoint.
    @Operation(summary = "Place an order and reduce stock")
    // Documents a possible status and its response body.
    @ApiResponse(responseCode = "201", description = "Order placed",
            content = @Content(schema = @Schema(implementation = OrderResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed or malformed JSON",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "User or product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Not enough stock",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        // 201 Created; the service may throw 404 or 409.
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    // Maps GET /slow; returns 200, demonstrates N+1 queries.
    @Operation(summary = "List orders (N+1 queries demo)")
    @GetMapping("/slow")
    public ResponseEntity<List<OrderResponse>> getAllSlow() {
        return ResponseEntity.ok(orderService.getAllOrdersSlow());
    }

    // GET /fast: same data as /slow, one query; 200.
    @Operation(summary = "List orders with one join fetch query")
    @GetMapping("/fast")
    public ResponseEntity<List<OrderResponse>> getAllFast() {
        return ResponseEntity.ok(orderService.getAllOrdersFast());
    }

    // GET /page?page=0&size=2&sort=createdAt,desc; returns 200.
    // Spring builds Pageable from the page, size, sort parameters.
    @Operation(summary = "List orders one page at a time")
    @GetMapping("/page")
    public ResponseEntity<PageResponse<OrderResponse>> getPage(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersPage(pageable));
    }
}
