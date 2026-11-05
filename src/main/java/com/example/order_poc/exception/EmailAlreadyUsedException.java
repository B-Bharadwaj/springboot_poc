package com.example.order_poc.exception;

// Thrown when registering an email that already has an account.
public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
