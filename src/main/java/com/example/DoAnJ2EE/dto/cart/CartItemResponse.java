package com.example.DoAnJ2EE.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long motorbikeId;
    private String motorbikeName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}