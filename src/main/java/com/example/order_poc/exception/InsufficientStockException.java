package com.example.order_poc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception: thrown when stock is too low for an order.
// Spring answers 409 Conflict when this escapes a controller.
@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends RuntimeException {

    // Passes the message up to RuntimeException.
    public InsufficientStockException(String message) {
        super(message);
    }
}
