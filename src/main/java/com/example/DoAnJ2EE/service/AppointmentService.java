package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.request.CreateAppointmentRequest;
import com.example.DoAnJ2EE.dto.request.UpdateAppointmentStatusRequest;
import com.example.DoAnJ2EE.dto.response.AdminAppointmentResponse;
import com.example.DoAnJ2EE.dto.response.AppointmentResponse;
import com.example.DoAnJ2EE.entity.User;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(User user, CreateAppointmentRequest request);
    List<AppointmentResponse> getMy(User user);
    List<AdminAppointmentResponse> getAllForAdmin();
    AdminAppointmentResponse getByIdForAdmin(Long id);
    AdminAppointmentResponse updateStatus(Long id, UpdateAppointmentStatusRequest request);
}