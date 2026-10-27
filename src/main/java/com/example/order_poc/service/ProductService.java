package com.example.order_poc.service;

import com.example.order_poc.dto.ProductRequest;
import com.example.order_poc.dto.ProductResponse;
import com.example.order_poc.exception.ResourceNotFoundException;
import com.example.order_poc.mapper.ProductMapper;
import com.example.order_poc.model.Product;
import com.example.order_poc.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service layer: product logic; calls the repository and the mapper.
// Spring creates and manages this class as a bean.
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Spring passes in the repository and mapper (constructor injection).
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> getAll() {
        // SELECT all products, then convert each to a response DTO.
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse getById(Long id) {
        // Unknown id throws ResourceNotFoundException, which returns 404.
        return productMapper.toResponse(findProduct(id));
    }

    public List<ProductResponse> searchByName(String name) {
        // Derived query: WHERE name = ?, exact and case-sensitive.
        return productRepository.findByName(name).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductResponse> findCheaperThan(double maxPrice) {
        // Derived query: WHERE price < ?, strictly less than.
        return productRepository.findByPriceLessThan(maxPrice).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        // id is null, so save() runs an INSERT.
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        // Fails with 404 if the product does not exist.
        findProduct(id);
        Product product = productMapper.toEntity(request);
        // id is set, so save() runs an UPDATE.
        product.setId(id);
        return productMapper.toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        // 404 if missing, instead of silently deleting nothing.
        findProduct(id);
        // Runs a DELETE for this row.
        productRepository.deleteById(id);
    }

    private Product findProduct(Long id) {
        // SELECT by id; lambda throws 404 when no row found.
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
}
