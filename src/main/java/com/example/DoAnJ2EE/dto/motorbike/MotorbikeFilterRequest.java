package com.example.DoAnJ2EE.dto.motorbike;

import lombok.Data;

@Data
public class MotorbikeFilterRequest {
    private Integer warehouseId;
    private Integer categoryId;
    private Integer brandId;
}