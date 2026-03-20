package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.checkout.CheckoutRequest;
import com.example.DoAnJ2EE.dto.checkout.CheckoutResponse;
import com.example.DoAnJ2EE.dto.paypal.PayPalCaptureResponse;
import com.example.DoAnJ2EE.dto.paypal.PayPalCreateOrderResponse;
import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    // ===== USER =====
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUser(User user);
    List<Order> getOrdersByCustomerEmail(String email);
    Order save(Order order);

    CheckoutResponse checkoutCod(User user, CheckoutRequest request);
    PayPalCreateOrderResponse createPaypalOrder(User user, CheckoutRequest request);
    PayPalCaptureResponse capturePaypalOrder(User user, Long orderId, String paypalOrderId);

    // ===== ADMIN =====
    List<Order> findAll();
    Order findById(Long id);
    Order updateStatus(Long id, String status);
}