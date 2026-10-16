package com.example.order_poc.mapper;

import com.example.order_poc.dto.ProductRequest;
import com.example.order_poc.dto.ProductResponse;
import com.example.order_poc.model.Product;
import org.springframework.stereotype.Component;

// @Component marks this class as a general-purpose Spring bean so it can be injected elsewhere.
@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return new Product(null, request.getName(), request.getPrice(), request.getQuantity());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }
}
