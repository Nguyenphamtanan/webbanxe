package com.example.DoAnJ2EE.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalMotorbikes;
    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private BigDecimal totalRevenue;
}
