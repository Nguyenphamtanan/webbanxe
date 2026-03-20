package com.example.DoAnJ2EE.dto.cart;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long motorbikeId;
    private Integer quantity;
}