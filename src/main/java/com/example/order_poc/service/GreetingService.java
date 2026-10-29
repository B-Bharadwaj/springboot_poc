package com.example.order_poc.service;

import com.example.order_poc.config.AppProperties;
import org.springframework.stereotype.Service;

// Service layer: returns the greeting text for /hello.
// Spring creates and manages this class as a bean.
@Service
public class GreetingService {

    private final AppProperties appProperties;

    // Spring passes in the typed config (constructor injection).
    public GreetingService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String greet() {
        return appProperties.greeting();
    }

    // Old Day 3 version using @Value, kept for comparison.
    // private final String greeting;
    //
    // public GreetingService(@Value("${app.greeting}") String greeting) {
    //     this.greeting = greeting;
    // }
    //
    // public String greet() {
    //     return greeting;
    // }
}
