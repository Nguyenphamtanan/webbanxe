package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
public class CustomerOrderApiController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getCustomerOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null || userDetails.getUser() == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "message", "Người dùng chưa đăng nhập"
                ));
            }

            String email = userDetails.getUser().getEmail();
            List<Order> orders = orderService.getOrdersByCustomerEmail(email);

            List<Map<String, Object>> response = orders.stream()
                    .map(order -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("id", order.getId());
                        map.put("orderCode", order.getOrderCode() != null ? order.getOrderCode() : "");
                        map.put("receiverName", order.getReceiverName() != null ? order.getReceiverName() : "");
                        map.put("receiverPhone", order.getReceiverPhone() != null ? order.getReceiverPhone() : "");
                        map.put("shippingAddress", order.getShippingAddress() != null ? order.getShippingAddress() : "");
                        map.put("note", order.getNote() != null ? order.getNote() : "");
                        map.put("totalAmount", order.getTotalAmount() != null ? order.getTotalAmount() : 0);
                        map.put("status", order.getStatus() != null ? order.getStatus().name() : "");
                        map.put("paymentMethod", order.getPaymentMethod() != null ? order.getPaymentMethod().name() : "");
                        map.put("paymentStatus", order.getPaymentStatus() != null ? order.getPaymentStatus().name() : "");
                        map.put("createdAt", order.getCreatedAt() != null ? order.getCreatedAt().toString() : "");
                        map.put("updatedAt", order.getUpdatedAt() != null ? order.getUpdatedAt().toString() : "");
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Không thể tải lịch sử đơn hàng: " + e.getMessage()
            ));
        }
    }
}