package com.example.DoAnJ2EE.dto.brand;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandResponse {
    private Integer id;
    private String name;
    private String slug;
}