package com.example.order_poc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entity: a customer who places orders.
// JPA maps this class to a database table.
@Entity
// Named "users" because USER is reserved in SQL.
@Table(name = "users")
// Lombok: generates getters.
@Getter
// Lombok: generates setters.
@Setter
// Lombok: empty constructor, required by JPA.
@NoArgsConstructor
public class User {

    // Primary key.
    @Id
    // Database auto-increments the id on INSERT.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}
