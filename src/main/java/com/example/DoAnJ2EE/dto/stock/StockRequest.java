package com.example.DoAnJ2EE.dto.stock;
import lombok.Data;

@Data
public class StockRequest {
    private Integer warehouseId;
    private Long motorbikeId;
    private Integer quantity;
}
