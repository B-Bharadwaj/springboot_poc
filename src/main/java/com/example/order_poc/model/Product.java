package com.example.order_poc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Getter generates a getter method for every field at compile time.
@Getter
// @Setter generates a setter method for every field at compile time.
@Setter
// @NoArgsConstructor generates an empty constructor (Jackson needs it to build objects from JSON).
@NoArgsConstructor
// @AllArgsConstructor generates a constructor that takes every field as a parameter.
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private double price;
    private int quantity;
}
