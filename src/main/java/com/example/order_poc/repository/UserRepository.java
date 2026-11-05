package com.example.order_poc.repository;

import com.example.order_poc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository layer: database access for users.
public interface UserRepository extends JpaRepository<User, Long> {

    // Derived query: SELECT ... WHERE email = ?
    Optional<User> findByEmail(String email);

    // Derived query: true if a user has this email.
    boolean existsByEmail(String email);
}
