package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MyPurchaseRequestResponse {

    private Long id;
    private String requestCode;
    private String requestType;
    private String status;

    private BigDecimal quotedPrice;
    private BigDecimal depositRequired;
    private String salesNote;

    private LocalDateTime createdAt;

    // 🔥 NEW
    private String motorbikeName;
    private String motorbikeImage;
    private String motorbikeSlug;
}