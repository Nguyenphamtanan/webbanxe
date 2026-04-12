package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminAppointmentResponse {

    private Long id;
    private String appointmentCode;

    private String customerName;
    private String phone;

    private String motorbikeName;
    private LocalDateTime appointmentTime;

    private String note;           // ghi chú của user
    private String status;

    private LocalDateTime createdAt;

    private String responseNote;   // 🔥 PHẢN HỒI CỦA ADMIN
}