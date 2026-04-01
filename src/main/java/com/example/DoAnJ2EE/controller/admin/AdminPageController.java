package com.example.DoAnJ2EE.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminPageController {

    @GetMapping("/admin/dashboard")
    public String adminDashboardPage() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/motorbikes")
    public String adminMotorbikePage() {
        return "admin/motorbike/list";
    }

    @GetMapping("/admin/motorbike/create")
    public String createMotorbikePage() {
        return "admin/motorbike/create";
    }

    @GetMapping("/admin/motorbike/edit/{id}")
    public String editMotorbikePage(@PathVariable Long id) {
        return "admin/motorbike/edit";
    }

    @GetMapping("/admin/motorbike/detail/{id}")
    public String detailMotorbikePage(@PathVariable Long id) {
        return "admin/motorbike/detail";
    }

    @GetMapping("/admin/categories")
    public String adminCategoryPage() {
        return "admin/category/list";
    }
    @GetMapping("/admin/categories/create")
    public String adminCategoryCreatePage() {
        return "admin/category/create";
    }

    @GetMapping("/admin/categories/edit")
    public String adminCategoryEditPage() {
        return "admin/category/edit";
    }

    @GetMapping("/admin/users")
    public String adminUserPage() {
        return "admin/user/list";
    }

    @GetMapping("/admin/orders")
    public String adminOrderPage() {
        return "admin/order/list";
    }

    @GetMapping("/admin/brands")
    public String adminBrandPage() {
        return "admin/brand/list";
    }

    @GetMapping("/admin/stocks")
    public String stockPage() {
        return "admin/stock/list";
    }

    @GetMapping("/admin/requests")
    public String adminRequests() {
        return "admin/request/list";
    }
    @GetMapping("/admin/requests/{id}")
    public String adminRequestDetailPage(@PathVariable Long id) {
        return "admin/request/detail";
    }
    @GetMapping("/admin/deposits")
    public String adminDepositPage() {
        return "admin/deposit/list";
    }
}