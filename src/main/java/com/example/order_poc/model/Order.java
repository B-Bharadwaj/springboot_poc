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

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: many orders belong to one user; LAZY means the user is only loaded when first accessed.
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn names the foreign-key column in the orders table that points to users.id.
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private String status;

    // @OneToMany: one order has many items; mappedBy says OrderItem.order owns the foreign key,
    // cascade ALL saves/deletes items with the order, orphanRemoval deletes items removed from this list.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
