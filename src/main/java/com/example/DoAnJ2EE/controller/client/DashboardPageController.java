package com.example.DoAnJ2EE.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardPageController {

    @GetMapping("/customer/dashboard")
    public String customerDashboardPage() {
        return "client/dashboard";
    }

    @GetMapping("/customer/profile")
    public String customerProfile() {
        return "client/profile";
    }

    @GetMapping("/customer/cart")
    public String customerCart() {
        return "client/cart";
    }

    @GetMapping("/customer/orders")
    public String customerOrders() {
        return "client/orders";
    }

    @GetMapping("/customer/checkout")
    public String customerCheckout() {
        return "client/checkout";
    }

    @GetMapping("/customer/requests")
    public String customerRequests() {
        return "client/requests";
    }
    @GetMapping("/showroom/compare")
    public String comparePage() {
        return "client/compare";
    }

    @GetMapping("/customer/deposits/{id}")
    public String customerDepositDetailPage(@PathVariable Long id) {
        return "client/deposit-detail";
    }
    @GetMapping("/customer/deposits/create")
    public String customerDepositCreatePage() {
        return "client/deposit-create";
    }

}

