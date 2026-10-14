package com.example.order_poc.controller;

import com.example.order_poc.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController marks this class as a web controller whose method return values are written directly to the HTTP response body.
@RestController
public class HelloController {

    private final GreetingService greetingService;

    // Single constructor: Spring injects the GreetingService bean automatically, no @Autowired needed.
    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // @GetMapping maps HTTP GET requests for "/hello" to this method.
    @GetMapping("/hello")
    public String hello() {
        return greetingService.greet();
    }
}
