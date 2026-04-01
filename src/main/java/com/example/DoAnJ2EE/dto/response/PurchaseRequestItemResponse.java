package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseRequestItemResponse {
    private Long id;
    private Long motorbikeId;
    private String motorbikeName;
    private String motorbikeSlug;
    private String motorbikeImage;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}