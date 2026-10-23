package com.example.order_poc.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entity: an order header; owns its order items.
// JPA maps this class to a database table.
@Entity
// Named "orders" because ORDER is reserved in SQL.
@Table(name = "orders")
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, required by JPA.
@NoArgsConstructor
public class Order {

    // Primary key.
    @Id
    // Database auto-increments the id on INSERT.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many orders per user; LAZY: user loaded on first access.
    @ManyToOne(fetch = FetchType.LAZY)
    // Foreign key column orders.user_id.
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private String status;

    // One order, many items; OrderItem.order owns the foreign key.
    // cascade ALL: saving or deleting the order includes its items.
    // orphanRemoval: items removed from this list get deleted.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
