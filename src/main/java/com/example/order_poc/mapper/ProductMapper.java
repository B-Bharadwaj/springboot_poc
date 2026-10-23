package com.example.order_poc.mapper;

import com.example.order_poc.dto.ProductRequest;
import com.example.order_poc.dto.ProductResponse;
import com.example.order_poc.model.Product;
import org.springframework.stereotype.Component;

// Mapper layer: converts between product DTOs and the entity.
// Spring creates and manages this class as a bean.
@Component
public class ProductMapper {

    // id null: the database assigns it on INSERT.
    public Product toEntity(ProductRequest request) {
        return new Product(null, request.getName(), request.getPrice(), request.getQuantity(),
                request.getDescription());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity(),
                product.getDescription());
    }
}
