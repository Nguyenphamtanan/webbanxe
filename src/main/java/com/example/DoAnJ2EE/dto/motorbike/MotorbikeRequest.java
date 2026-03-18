package com.example.DoAnJ2EE.dto.motorbike;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MotorbikeRequest {
    private String name;
    private String slug;
    private String sku;
    private BigDecimal price;
    private String description;
    private String engine;
    private BigDecimal weightKg;
    private String primaryImageUrl;
    private Integer brandId;
    private Integer categoryId;
    private Boolean isActive;
}