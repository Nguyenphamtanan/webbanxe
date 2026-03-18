package com.example.DoAnJ2EE.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardPageController {

    @GetMapping("/customer/dashboard")
    public String customerDashboardPage() {
        return "client/dashboard";
    }
}