package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private Long orderId;
    private String orderCode;
    private String paymentMethod;
    private String paymentStatus;
    private String message;
}