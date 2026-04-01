package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteRequestUpdateRequest {
    private BigDecimal quotedPrice;
    private BigDecimal depositRequired;
    private String salesNote;
}