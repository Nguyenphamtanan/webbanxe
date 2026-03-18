package com.example.DoAnJ2EE.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDashboardResponse {
    private String fullName;
    private String email;
    private String phone;

    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private long cancelledOrders;
}
