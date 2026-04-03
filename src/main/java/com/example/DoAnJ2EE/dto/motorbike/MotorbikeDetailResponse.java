package com.example.DoAnJ2EE.dto.motorbike;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MotorbikeDetailResponse {
    private Long id;
    private String name;
    private String slug;
    private String sku;
    private BigDecimal price;
    private String description;
    private String engine;
    private BigDecimal weightKg;
    private String primaryImageUrl;

    private Integer brandId;
    private String brandName;

    private Integer categoryId;
    private String categoryName;

    private Boolean isActive;
}