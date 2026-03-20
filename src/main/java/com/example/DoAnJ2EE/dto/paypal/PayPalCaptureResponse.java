package com.example.DoAnJ2EE.dto.paypal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayPalCaptureResponse {
    private Long orderId;
    private String orderCode;
    private String paypalOrderId;
    private String paymentStatus;
    private String status;
}