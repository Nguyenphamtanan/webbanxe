package com.example.DoAnJ2EE.service;


import com.example.DoAnJ2EE.dto.request.CheckoutRequest;
import com.example.DoAnJ2EE.dto.response.CheckoutResponse;
import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.dto.response.MyOrderResponse;


import java.util.List;
import java.util.Optional;

public interface OrderService {

    // ===== USER =====
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUser(User user);
    Order save(Order order);
    CheckoutResponse checkoutCod(User user, CheckoutRequest request);
    List<MyOrderResponse> getMyOrders(User user);



    // ===== ADMIN =====
    List<Order> findAll();
    Order findById(Long id);
    Order updateStatus(Long id, String status);
}