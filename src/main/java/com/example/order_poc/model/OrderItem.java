package com.example.order_poc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entity: one order line linking an order and a product.
// JPA maps this class to a database table.
@Entity
// Table name in the database.
@Table(name = "order_items")
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, required by JPA.
@NoArgsConstructor
public class OrderItem {

    // Primary key.
    @Id
    // Database auto-increments the id on INSERT.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many items per order; LAZY: order loaded on first access.
    @ManyToOne(fetch = FetchType.LAZY)
    // Foreign key column order_items.order_id.
    @JoinColumn(name = "order_id")
    private Order order;

    // LAZY product; no cascade, so items never change products.
    @ManyToOne(fetch = FetchType.LAZY)
    // Foreign key column order_items.product_id.
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    // Price at order time, kept if the product price changes.
    private double unitPrice;
}
