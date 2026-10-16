package com.example.order_poc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// @Service marks this class as a service-layer bean; Spring creates one instance and can inject it elsewhere.
@Service
public class GreetingService {

    private final String greeting;

    // @Value injects the app.greeting setting from application.yml into this constructor parameter.
    public GreetingService(@Value("${app.greeting}") String greeting) {
        this.greeting = greeting;
    }

    public String greet() {
        return greeting;
    }
}
