package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUser(User user);
    Order save(Order order);
}