package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MotorbikeService motorbikeService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("motorbikes", motorbikeService.getAll());
        return "client/home";
    }

    @GetMapping("/customer/cart")
    public String cartPage() {
        return "client/cart";
    }

    @GetMapping("/customer/checkout")
    public String checkoutPage() {
        return "client/checkout";
    }

    @GetMapping("/customer/dashboard")
    public String dashboardPage() {
        return "client/dashboard";
    }

    @GetMapping("/customer/profile")
    public String profilePage() {
        return "client/profile";
    }
    @GetMapping("/customer/orders")
    public String ordersPage() {
        return "client/orders";
    }
}