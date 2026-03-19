package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.order.OrderDetailResponse;
import com.example.DoAnJ2EE.dto.order.OrderItemResponse;
import com.example.DoAnJ2EE.dto.order.OrderResponse;
import com.example.DoAnJ2EE.dto.order.UpdateOrderStatusRequest;
import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.OrderItem;
import com.example.DoAnJ2EE.repository.OrderItemRepository;
import com.example.DoAnJ2EE.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderApiController {

    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderDetailResponse getOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id);

        List<OrderItemResponse> items = orderItemRepository.findByOrder(order).stream()
                .map(this::mapToItemResponse)
                .toList();

        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .customerName(order.getUser() != null ? order.getUser().getFullName() : "")
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .shippingAddress(order.getShippingAddress())
                .note(order.getNote())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @RequestBody UpdateOrderStatusRequest request) {
        Order order = orderService.updateStatus(id, request.getStatus());
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .customerName(order.getUser() != null ? order.getUser().getFullName() : "")
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .shippingAddress(order.getShippingAddress())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .motorbikeName(item.getMotorbikeName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .lineTotal(item.getLineTotal())
                .build();
    }
}