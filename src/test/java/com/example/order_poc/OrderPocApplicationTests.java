package com.example.order_poc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Smoke test: passes if the whole application starts.
// Starts the full Spring context, including the database.
@SpringBootTest
class OrderPocApplicationTests {

	// JUnit test; empty body, successful startup is the check.
	@Test
	void contextLoads() {
	}

}
