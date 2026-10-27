package com.example.order_poc.exception;

// Exception: thrown by services when an id is not found.
// GlobalExceptionHandler turns this into a 404 response.
public class ResourceNotFoundException extends RuntimeException {

    // Passes the message up to RuntimeException.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
