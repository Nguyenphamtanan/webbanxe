package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.admin.AdminDashboardResponse;
import com.example.DoAnJ2EE.dto.user.CustomerDashboardResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    public AdminDashboardResponse adminDashboard() {
        return dashboardService.getAdminDashboard();
    }

    @GetMapping("/customer")
    public CustomerDashboardResponse customerDashboard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return dashboardService.getCustomerDashboard(userDetails.getUser().getEmail());
    }
}
