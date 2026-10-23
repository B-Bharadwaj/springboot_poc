package com.example.order_poc.repository;

import com.example.order_poc.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository layer: database access for products.
// Spring Data generates the implementation; CRUD methods are inherited.
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Derived query: WHERE name = ?
    List<Product> findByName(String name);

    // Derived query: WHERE price < ?
    List<Product> findByPriceLessThan(double price);
}
