package com.example.DoAnJ2EE.common.constant;

public enum OrderStatus {
    WAITING_CONFIRMATION,   // chờ admin xác nhận
    WAITING_DEPOSIT,        // chờ cọc
    DEPOSIT_PAID,           // đã cọc
    WAITING_FINAL_PAYMENT,  // chờ thanh toán phần còn lại
    PAID,                   // đã thanh toán đủ
    PREPARING_DELIVERY,     // chuẩn bị giao xe
    DELIVERED,              // đã giao xe
    CANCELLED
}