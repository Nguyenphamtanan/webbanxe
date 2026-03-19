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

    // ===== USER =====
    @Override
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // ===== ADMIN =====
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
    }

    @Override
    public Order updateStatus(Long id, String status) {
        Order order = findById(id);

        try {
            order.setStatus(
                    com.example.DoAnJ2EE.common.constant.OrderStatus.valueOf(status.toUpperCase())
            );
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }

        return orderRepository.save(order);
    }
}