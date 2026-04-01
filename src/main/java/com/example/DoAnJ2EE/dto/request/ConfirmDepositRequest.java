package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfirmDepositRequest {
    private BigDecimal depositAmount;
    private String salesNote;
}