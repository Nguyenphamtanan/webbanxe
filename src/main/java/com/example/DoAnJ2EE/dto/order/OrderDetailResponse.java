package com.example.DoAnJ2EE.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {
    private Long id;
    private String orderCode;
    private String customerName;
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    private String note;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}