package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long motorbikeId;
    private Integer quantity;
}