package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class UpdateDepositStatusRequest {
    private String status;
    private String note;
}