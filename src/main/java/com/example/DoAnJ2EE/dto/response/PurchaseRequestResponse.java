package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseRequestResponse {
    private Long id;
    private String requestCode;
    private Integer regionId;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String note;
    private String requestType;
    private String status;
    private BigDecimal quotedPrice;
    private BigDecimal depositRequired;
    private String salesNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PurchaseRequestItemResponse> items;
}