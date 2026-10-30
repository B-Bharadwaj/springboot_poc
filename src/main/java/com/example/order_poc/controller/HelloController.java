package com.example.order_poc.controller;

import com.example.order_poc.service.GreetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller layer: GET /hello; calls the greeting service.
// Handles HTTP; return values become the response body.
@RestController
// Swagger UI groups this endpoint under "Hello".
@Tag(name = "Hello", description = "Greeting endpoint")
public class HelloController {

    private final GreetingService greetingService;

    // Spring passes in the GreetingService (constructor injection).
    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // Maps GET /hello to this method; returns 200.
    // One-line description shown next to the endpoint.
    @Operation(summary = "Return the greeting text")
    @GetMapping("/hello")
    public String hello() {
        return greetingService.greet();
    }
}
