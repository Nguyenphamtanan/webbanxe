package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}