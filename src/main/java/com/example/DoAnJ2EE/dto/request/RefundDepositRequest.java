package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class RefundDepositRequest {
    private String reason;
    private String toBin;
    private String toAccountNumber;
}