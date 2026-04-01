package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderTransactionResponse {
    private Long id;
    private String orderCode;
    private BigDecimal totalAmount;
    private BigDecimal quotedPrice;
    private BigDecimal depositAmount;
    private BigDecimal remainingAmount;
    private String status;
    private String paymentStatus;
    private String salesNote;
    private LocalDateTime createdAt;
}