package com.example.order_poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

// Entry point: starts Spring Boot and the web server.
// Enables auto-configuration and scans this package for beans.
// Exclude: no default user, so no generated password logged.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
// Finds @ConfigurationProperties records and registers them as beans.
@ConfigurationPropertiesScan
public class OrderPocApplication {

	public static void main(String[] args) {
		// Creates beans, runs Flyway, opens port 8081.
		SpringApplication.run(OrderPocApplication.class, args);
	}

}
