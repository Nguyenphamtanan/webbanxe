package com.example.DoAnJ2EE.dto.request;

import lombok.Data;

@Data
public class UpdateAppointmentStatusRequest {
    private String status;
    private String responseNote;
}