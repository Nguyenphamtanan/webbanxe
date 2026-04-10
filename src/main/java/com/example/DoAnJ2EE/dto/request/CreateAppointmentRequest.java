package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    private Long motorbikeId;
    private String fullName;
    private String phone;
    private LocalDateTime appointmentTime;
    private String note;
}