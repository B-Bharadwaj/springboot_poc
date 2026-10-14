package com.example.order_poc.controller;

import com.example.order_poc.model.Product;
import com.example.order_poc.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// @RequestMapping sets the base URL path shared by every endpoint in this controller.
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    // @PathVariable binds the {id} part of the URL to the method parameter.
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // @PostMapping maps HTTP POST requests to this method.
    // @RequestBody converts the JSON request body into a Product object.
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    // @PutMapping maps HTTP PUT requests to this method.
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    // @DeleteMapping maps HTTP DELETE requests to this method.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
