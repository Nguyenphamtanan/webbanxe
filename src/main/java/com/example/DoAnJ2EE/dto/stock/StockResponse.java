package com.example.DoAnJ2EE.dto.stock;
import lombok.Data;
import lombok.Builder;

@Data
@Builder

public class StockResponse {
    private Long id;
    private String warehouseName;
    private String motorbikeName;
    private Integer quantity;
}
