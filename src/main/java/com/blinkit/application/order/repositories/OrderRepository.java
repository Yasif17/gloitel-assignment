package com.blinkit.application.order.repositories;

import com.blinkit.application.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByPlacedAtDesc(Long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);   // ownership check baked into the query itself
}
