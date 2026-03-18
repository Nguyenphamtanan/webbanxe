package com.example.DoAnJ2EE.dto.motorbike;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MotorbikeResponse {
    private Long id;
    private String name;
    private String slug;
    private String sku;
    private BigDecimal price;
    private String primaryImageUrl;
    private String brandName;
    private String categoryName;
    private Boolean isActive;
}