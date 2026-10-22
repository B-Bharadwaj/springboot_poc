package com.example.order_poc.repository;

import com.example.order_poc.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // @Query defines the JPQL query for this method instead of deriving it from the method name.
    @Query("select distinct o from Order o join fetch o.user join fetch o.items i join fetch i.product")
    List<Order> findAllWithItems();

    // @EntityGraph makes this query also fetch the listed associations (here: user) in the same SQL via a join.
    @EntityGraph(attributePaths = "user")
    @Override
    Page<Order> findAll(Pageable pageable);
}
