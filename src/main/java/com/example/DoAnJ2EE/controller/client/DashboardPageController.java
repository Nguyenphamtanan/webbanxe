package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardPageController {

    private final MotorbikeRepository motorbikeRepository;

    @GetMapping("/customer/dashboard")
    public String customerDashboardPage() {
        return "redirect:/customer/profile";
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
    public String customerDepositDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("depositId", id);
        return "client/deposit-detail";
    }

    @GetMapping("/customer/deposits/create")
    public String customerDepositCreatePage() {
        return "client/deposit-create";
    }

    @GetMapping("/customer/deposits/return")
    public String depositReturnPage(@RequestParam(required = false) Long depositId) {
        if (depositId != null) {
            return "redirect:/customer/deposits/" + depositId;
        }
        return "redirect:/";
    }

    @GetMapping("/customer/deposits/cancel")
    public String depositCancelPage(@RequestParam(required = false) Long depositId) {
        if (depositId != null) {
            return "redirect:/customer/deposits/" + depositId;
        }
        return "redirect:/";
    }

    @GetMapping("/customer/appointments/create")
    public String customerAppointmentCreatePage(@RequestParam Long motorbikeId, Model model) {
        var bike = motorbikeRepository.findById(motorbikeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        model.addAttribute("bike", bike);
        return "client/appointment-request";
    }

    @GetMapping("/customer/appointments")
    public String customerAppointmentsPage() {
        return "client/appointments";
    }
}