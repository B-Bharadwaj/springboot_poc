package com.example.order_poc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Service layer: returns the greeting text for /hello.
// Spring creates and manages this class as a bean.
@Service
public class GreetingService {

    private final String greeting;

    // @Value injects app.greeting from application.yml at startup.
    public GreetingService(@Value("${app.greeting}") String greeting) {
        this.greeting = greeting;
    }

    public String greet() {
        return greeting;
    }
}
