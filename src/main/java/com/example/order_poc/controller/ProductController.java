package com.example.order_poc.controller;

import com.example.order_poc.dto.ProductRequest;
import com.example.order_poc.dto.ProductResponse;
import com.example.order_poc.service.ProductService;
import com.example.order_poc.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
// Swagger UI groups these endpoints under "Products".
@Tag(name = "Products", description = "Create, read, update and delete products")
public class ProductController {

    private final ProductService productService;

    // Spring passes in the ProductService (constructor injection).
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Maps GET /api/products; returns 200 with all products.
    // One-line description shown next to the endpoint.
    @Operation(summary = "List all products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    // GET /search?name=...; 200, or 400 if name is missing.
    // @RequestParam reads ?name= from the URL; required by default.
    @Operation(summary = "Search products by name")
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    // GET /cheap?maxPrice=...; 200, or 400 if not a number.
    @Operation(summary = "List products cheaper than maxPrice")
    @GetMapping("/cheap")
    public ResponseEntity<List<ProductResponse>> findCheaperThan(@RequestParam double maxPrice) {
        return ResponseEntity.ok(productService.findCheaperThan(maxPrice));
    }

    // GET /{id}: 200, or 404 if the product doesn't exist.
    // @PathVariable takes {id} from the URL path.
    @Operation(summary = "Get a product by id")
    // Documents a possible status and its response body.
    @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Id is not a number",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // Maps POST; @RequestBody turns the JSON into a ProductRequest.
    // @Valid checks the DTO's rules first; failure returns 400.
    @Operation(summary = "Create a product")
    @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed or malformed JSON",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        // 201 Created with the new product.
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    // Maps PUT /{id}: 200, or 404 if not found.
    @Operation(summary = "Update a product")
    @ApiResponse(responseCode = "200", description = "Product updated",
            content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed or malformed JSON",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    // Maps DELETE /{id}: 404 if not found.
    @Operation(summary = "Delete a product")
    @ApiResponse(responseCode = "204", description = "Product deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        // 204 No Content: success, empty body.
        return ResponseEntity.noContent().build();
    }
}
