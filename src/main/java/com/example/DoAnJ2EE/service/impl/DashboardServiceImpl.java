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
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final MotorbikeRepository motorbikeRepository;
    private final OrderRepository orderRepository;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);

        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return AdminDashboardResponse.builder()
                .totalUsers(userRepository.countByIsActiveTrue())
                .totalMotorbikes(motorbikeRepository.countByIsActiveTrue())
                .totalOrders(orderRepository.count())
                .pendingOrders(orderRepository.countByStatus(OrderStatus.PENDING))
                .completedOrders(orderRepository.countByStatus(OrderStatus.COMPLETED))
                .totalRevenue(totalRevenue)
                .build();
    }

    @Override
    public CustomerDashboardResponse getCustomerDashboard(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        return CustomerDashboardResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .totalOrders(orderRepository.countByUser(user))
                .pendingOrders(orderRepository.countByUserAndStatus(user, OrderStatus.PENDING))
                .completedOrders(orderRepository.countByUserAndStatus(user, OrderStatus.COMPLETED))
                .cancelledOrders(orderRepository.countByUserAndStatus(user, OrderStatus.CANCELLED))
                .build();
    }
}
