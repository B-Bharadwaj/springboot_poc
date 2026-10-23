package com.example.order_poc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception: thrown by services when an id is not found.
// Spring answers 404 Not Found when this escapes a controller.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Passes the message up to RuntimeException.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
