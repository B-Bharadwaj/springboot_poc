package com.example.order_poc.repository;

import com.example.order_poc.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Repository layer: database access for orders.
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Custom JPQL query instead of a derived one.
    // One SELECT that join-fetches user, items and products.
    // distinct removes duplicate orders produced by the join.
    @Query("select distinct o from Order o join fetch o.user join fetch o.items i join fetch i.product")
    List<Order> findAllWithItems();

    // Joins user into the page query; items load in batches.
    @EntityGraph(attributePaths = "user")
    // Replaces the inherited findAll(Pageable) to add the graph.
    @Override
    Page<Order> findAll(Pageable pageable);
}
