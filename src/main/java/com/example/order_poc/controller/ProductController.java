package com.example.order_poc.controller;

import com.example.order_poc.dto.ProductRequest;
import com.example.order_poc.dto.ProductResponse;
import com.example.order_poc.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller layer: product CRUD endpoints; calls the product service.
// Handles HTTP; return values become the response body.
@RestController
// All endpoints here start with /api/products.
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Spring passes in the ProductService (constructor injection).
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Maps GET /api/products; returns 200 with all products.
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    // GET /search?name=...; 200, or 400 if name is missing.
    // @RequestParam reads ?name= from the URL; required by default.
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    // GET /cheap?maxPrice=...; 200, or 400 if not a number.
    @GetMapping("/cheap")
    public ResponseEntity<List<ProductResponse>> findCheaperThan(@RequestParam double maxPrice) {
        return ResponseEntity.ok(productService.findCheaperThan(maxPrice));
    }

    // GET /{id}: 200, or 404 if the product doesn't exist.
    // @PathVariable takes {id} from the URL path.
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // Maps POST; @RequestBody turns the JSON into a ProductRequest.
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        // 201 Created with the new product.
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    // Maps PUT /{id}: 200, or 404 if not found.
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    // Maps DELETE /{id}: 404 if not found.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        // 204 No Content: success, empty body.
        return ResponseEntity.noContent().build();
    }
}
