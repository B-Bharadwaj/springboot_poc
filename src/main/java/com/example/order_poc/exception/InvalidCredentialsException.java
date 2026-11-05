package com.example.order_poc.exception;

// Thrown on wrong email or password; one message for both.
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
