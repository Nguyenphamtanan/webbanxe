package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MyOrderResponse {
    private Long id;
    private String orderCode;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    private LocalDateTime createdAt;
}