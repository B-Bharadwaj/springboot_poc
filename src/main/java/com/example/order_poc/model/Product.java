package com.example.order_poc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity marks this class as a JPA entity: each instance is a row in a database table.
@Entity
// @Table sets the name of the database table this entity is stored in.
@Table(name = "products")
// @Getter generates a getter method for every field at compile time.
@Getter
// @Setter generates a setter method for every field at compile time.
@Setter
// @NoArgsConstructor generates an empty constructor (Jackson needs it to build objects from JSON).
@NoArgsConstructor
// @AllArgsConstructor generates a constructor that takes every field as a parameter.
@AllArgsConstructor
public class Product {

    // @Id marks this field as the table's primary key.
    @Id
    // @GeneratedValue with IDENTITY lets the database auto-increment the id on insert.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private int quantity;
}
