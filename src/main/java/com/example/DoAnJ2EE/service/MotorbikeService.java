package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeRequest;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;

import java.util.List;

public interface MotorbikeService {
    List<MotorbikeResponse> getAll();
    MotorbikeDetailResponse getById(Long id);
    MotorbikeDetailResponse getBySlug(String slug);
    MotorbikeResponse create(MotorbikeRequest request);
    MotorbikeResponse update(Long id, MotorbikeRequest request);
    void delete(Long id);
}