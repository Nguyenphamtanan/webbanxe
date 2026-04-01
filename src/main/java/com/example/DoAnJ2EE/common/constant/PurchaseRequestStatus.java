package com.example.DoAnJ2EE.common.constant;

public enum PurchaseRequestStatus {
    NEW,          // mới gửi
    CONTACTED,    // đã liên hệ
    QUOTED,       // đã báo giá
    APPROVED,     // khách đồng ý
    REJECTED,     // từ chối
    CONVERTED     // đã chuyển thành order
}