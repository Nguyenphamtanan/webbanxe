package com.example.DoAnJ2EE.dto.paypal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayPalCreateOrderResponse {
    private Long localOrderId;
    private String localOrderCode;
    private String paypalOrderId;
    private String approveUrl;
}