package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class CreateDepositRequest {
    private Long motorbikeId;
    private String note;
}