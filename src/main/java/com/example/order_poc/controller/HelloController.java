package com.example.order_poc.controller;

import com.example.order_poc.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller layer: GET /hello; calls the greeting service.
// Handles HTTP; return values become the response body.
@RestController
public class HelloController {

    private final GreetingService greetingService;

    // Spring passes in the GreetingService (constructor injection).
    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // Maps GET /hello to this method; returns 200.
    @GetMapping("/hello")
    public String hello() {
        return greetingService.greet();
    }
}
