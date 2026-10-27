package com.example.order_poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Entry point: starts Spring Boot and the web server.
// Enables auto-configuration and scans this package for beans.
@SpringBootApplication
public class OrderPocApplication {

	public static void main(String[] args) {
		// Creates beans, runs Flyway, opens port 8081.
		SpringApplication.run(OrderPocApplication.class, args);
	}

}
