package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DepositResponse {
    private Long id;
    private String depositCode;

    private Long requestId;
    private String requestCode;

    private Long motorbikeId;
    private String motorbikeName;
    private String motorbikeSlug;
    private String motorbikeImage;

    private BigDecimal quotedPrice;
    private BigDecimal depositAmount;

    private String paymentMethod;
    private String paymentStatus;
    private String status;

    private String note;
    private LocalDateTime paidAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime createdAt;

    private String bankCode;
    private String bankAccount;
    private String bankName;
    private String accountName;

    private Long payosOrderCode;
    private String checkoutUrl;
    private String qrCode;
    private String transactionCode;


}