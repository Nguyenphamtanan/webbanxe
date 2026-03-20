package com.example.DoAnJ2EE.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> items;
    private Integer totalItems;
    private BigDecimal totalAmount;
}