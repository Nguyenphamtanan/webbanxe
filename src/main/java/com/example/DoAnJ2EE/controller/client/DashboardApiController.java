package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.admin.AdminDashboardResponse;
import com.example.DoAnJ2EE.dto.user.CustomerDashboardResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public CustomerDashboardResponse customerDashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Chưa đăng nhập");
        }

        String email;

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            email = customUserDetails.getUser().getEmail();
        } else if (principal instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
        } else {
            email = authentication.getName();
        }

        System.out.println("=== DEBUG DASHBOARD EMAIL: " + email);

        return dashboardService.getCustomerDashboard(email);
    }
}