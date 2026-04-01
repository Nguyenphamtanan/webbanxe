package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class CreatePurchaseRequestRequest {
    private Integer regionId;
    private Long motorbikeId;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String note;
    private String requestType; // QUOTE / VIEWING / DEPOSIT / CONSULT
}