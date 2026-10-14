package com.example.order_poc.service;

import org.springframework.stereotype.Service;

// @Service marks this class as a service-layer bean; Spring creates one instance and can inject it elsewhere.
@Service
public class GreetingService {

    public String greet() {
        return "Hello from the service";
    }
}
