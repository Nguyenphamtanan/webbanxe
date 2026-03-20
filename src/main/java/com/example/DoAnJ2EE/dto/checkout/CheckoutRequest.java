package com.example.DoAnJ2EE.dto.checkout;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    private Integer regionId;
    private String note;
}