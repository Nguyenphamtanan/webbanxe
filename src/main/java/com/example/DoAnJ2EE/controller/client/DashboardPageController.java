package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.common.constant.DepositStatus;
import com.example.DoAnJ2EE.common.constant.PaymentStatus;
import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.repository.DepositRepository;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class DashboardPageController {

    private final MotorbikeRepository motorbikeRepository;
    private final DepositRepository depositRepository;

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
    public String customerDepositDetailPage(@PathVariable Long id,
                                            @RequestParam(required = false) String payment,
                                            Model model) {
        model.addAttribute("depositId", id);
        model.addAttribute("payment", payment);
        return "client/deposit-detail";
    }

    @GetMapping("/customer/deposits/create")
    public String customerDepositCreatePage() {
        return "client/deposit-create";
    }

    @Transactional
    @GetMapping("/customer/deposits/return")
    public String depositReturnPage(@RequestParam(required = false) Long depositId) {
        if (depositId != null) {
            Deposit deposit = depositRepository.findById(depositId).orElse(null);

            if (deposit != null) {
                System.out.println("=== RETURN URL HIT ===");
                System.out.println("depositId = " + depositId);
                System.out.println("before return update - paymentStatus = " + deposit.getPaymentStatus());
                System.out.println("before return update - status = " + deposit.getStatus());

                if (deposit.getPaymentStatus() == PaymentStatus.UNPAID
                        && deposit.getStatus() == DepositStatus.PENDING) {

                    deposit.setPaymentStatus(PaymentStatus.DEPOSIT_PAID);
                    deposit.setStatus(DepositStatus.PAID);
                    deposit.setPaidAt(LocalDateTime.now());

                    depositRepository.save(deposit);

                    System.out.println("Fallback return-url đã cập nhật deposit sang PAID");
                }
            }

            return "redirect:/customer/deposits/" + depositId + "?payment=success";
        }
        return "redirect:/";
    }

    @GetMapping("/customer/deposits/cancel")
    public String depositCancelPage(@RequestParam(required = false) Long depositId) {
        if (depositId != null) {
            return "redirect:/customer/deposits/" + depositId + "?payment=cancel";
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
    @GetMapping("/customer/refunds")
    public String customerRefundsPage(Model model) {
        model.addAttribute("activePage", "refunds");
        return "client/refunds";
    }
}