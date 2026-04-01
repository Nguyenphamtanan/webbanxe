package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long cartItemId;
    private Integer quantity;
}