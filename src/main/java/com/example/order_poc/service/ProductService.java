package com.example.order_poc.service;

import com.example.order_poc.model.Product;
import com.example.order_poc.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public Product create(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        getById(id);
        product.setId(id);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        getById(id);
        productRepository.deleteById(id);
    }
}
