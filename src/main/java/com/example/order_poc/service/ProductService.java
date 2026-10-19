package com.example.order_poc.service;

import com.example.order_poc.dto.ProductRequest;
import com.example.order_poc.dto.ProductResponse;
import com.example.order_poc.exception.ResourceNotFoundException;
import com.example.order_poc.mapper.ProductMapper;
import com.example.order_poc.model.Product;
import com.example.order_poc.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse getById(Long id) {
        return productMapper.toResponse(findProduct(id));
    }

    public List<ProductResponse> searchByName(String name) {
        return productRepository.findByName(name).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductResponse> findCheaperThan(double maxPrice) {
        return productRepository.findByPriceLessThan(maxPrice).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        findProduct(id);
        Product product = productMapper.toEntity(request);
        product.setId(id);
        return productMapper.toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        findProduct(id);
        productRepository.deleteById(id);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
}
