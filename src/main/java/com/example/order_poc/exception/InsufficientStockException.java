package com.example.order_poc.exception;

// Exception: thrown when stock is too low for an order.
// GlobalExceptionHandler turns this into a 409 response.
public class InsufficientStockException extends RuntimeException {

    // Passes the message up to RuntimeException.
    public InsufficientStockException(String message) {
        super(message);
    }
}
