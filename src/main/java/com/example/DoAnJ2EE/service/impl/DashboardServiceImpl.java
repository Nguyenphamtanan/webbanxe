package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.OrderStatus;
import com.example.DoAnJ2EE.dto.admin.AdminDashboardResponse;
import com.example.DoAnJ2EE.dto.user.CustomerDashboardResponse;
import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.repository.OrderRepository;
import com.example.DoAnJ2EE.repository.UserRepository;
import com.example.DoAnJ2EE.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final MotorbikeRepository motorbikeRepository;
    private final OrderRepository orderRepository;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        long pendingOrders =
                orderRepository.countByStatus(OrderStatus.WAITING_CONFIRMATION)
                        + orderRepository.countByStatus(OrderStatus.WAITING_DEPOSIT)
                        + orderRepository.countByStatus(OrderStatus.DEPOSIT_PAID)
                        + orderRepository.countByStatus(OrderStatus.WAITING_FINAL_PAYMENT)
                        + orderRepository.countByStatus(OrderStatus.PREPARING_DELIVERY);

        long completedOrdersCount =
                orderRepository.countByStatus(OrderStatus.PAID)
                        + orderRepository.countByStatus(OrderStatus.DELIVERED);

        List<Order> revenueOrders = new ArrayList<>();
        revenueOrders.addAll(orderRepository.findByStatus(OrderStatus.PAID));
        revenueOrders.addAll(orderRepository.findByStatus(OrderStatus.DELIVERED));

        BigDecimal totalRevenue = revenueOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return AdminDashboardResponse.builder()
                .totalUsers(userRepository.countByIsActiveTrue())
                .totalMotorbikes(motorbikeRepository.countByIsActiveTrue())
                .totalOrders(orderRepository.count())
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrdersCount)
                .totalRevenue(totalRevenue)
                .build();
    }

    @Override
    public CustomerDashboardResponse getCustomerDashboard(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        long pendingOrders =
                orderRepository.countByUserAndStatus(user, OrderStatus.WAITING_CONFIRMATION)
                        + orderRepository.countByUserAndStatus(user, OrderStatus.WAITING_DEPOSIT)
                        + orderRepository.countByUserAndStatus(user, OrderStatus.DEPOSIT_PAID)
                        + orderRepository.countByUserAndStatus(user, OrderStatus.WAITING_FINAL_PAYMENT)
                        + orderRepository.countByUserAndStatus(user, OrderStatus.PREPARING_DELIVERY);

        long completedOrders =
                orderRepository.countByUserAndStatus(user, OrderStatus.PAID)
                        + orderRepository.countByUserAndStatus(user, OrderStatus.DELIVERED);

        long cancelledOrders =
                orderRepository.countByUserAndStatus(user, OrderStatus.CANCELLED);

        return CustomerDashboardResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .totalOrders(orderRepository.countByUser(user))
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .build();
    }
}