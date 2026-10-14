package com.example.order_poc.repository;

import com.example.order_poc.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Repository marks this class as a data-access bean; Spring creates one instance and can inject it elsewhere.
@Repository
public class ProductRepository {

    private final List<Product> products = new ArrayList<>();
    private long nextId = 1;

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(nextId++);
            products.add(product);
            return product;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                return product;
            }
        }
        products.add(product);
        return product;
    }

    public void deleteById(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}
