package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class UpdatePurchaseRequestStatusRequest {
    private String status; // NEW / CONTACTED / QUOTED / APPROVED / REJECTED / CONVERTED
    private String salesNote;
}