package com.example.DoAnJ2EE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private String appointmentCode;
    private String motorbikeName;
    private LocalDateTime appointmentTime;
    private String status;
}