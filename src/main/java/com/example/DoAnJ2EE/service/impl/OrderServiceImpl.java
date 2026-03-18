package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.OrderRepository;
import com.example.DoAnJ2EE.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}