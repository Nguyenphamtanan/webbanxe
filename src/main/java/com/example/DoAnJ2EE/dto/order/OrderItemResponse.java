package com.example.DoAnJ2EE.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private Long id;
    private String motorbikeName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}