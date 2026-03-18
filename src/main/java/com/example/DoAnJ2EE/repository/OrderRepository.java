package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.common.constant.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByStatus(OrderStatus status);

    long countByStatus(OrderStatus status);
    long countByUser(User user);
    long countByUserAndStatus(User user, OrderStatus status);
}