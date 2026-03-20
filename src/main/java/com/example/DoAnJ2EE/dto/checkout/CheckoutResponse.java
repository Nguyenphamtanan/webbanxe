package com.example.DoAnJ2EE.dto.checkout;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CheckoutResponse {
    private Long orderId;
    private String orderCode;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private BigDecimal totalAmount;
}