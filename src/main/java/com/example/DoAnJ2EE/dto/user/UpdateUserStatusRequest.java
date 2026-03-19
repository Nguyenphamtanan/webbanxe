package com.example.DoAnJ2EE.dto.user;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private Boolean isActive;
}