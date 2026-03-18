package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.admin.AdminDashboardResponse;
import com.example.DoAnJ2EE.dto.user.CustomerDashboardResponse;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();
    CustomerDashboardResponse getCustomerDashboard(String email);
}
