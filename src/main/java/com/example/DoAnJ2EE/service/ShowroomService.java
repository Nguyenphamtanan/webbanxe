package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;

import java.util.List;

public interface ShowroomService {

    List<MotorbikeResponse> filter(Integer warehouseId,
                                   Integer categoryId,
                                   Integer brandId,
                                   String keyword,
                                   String sortBy,
                                   String sortDir);

    MotorbikeDetailResponse getDetailBySlug(String slug);

    List<MotorbikeDetailResponse> getCompareItems(List<Long> ids);
    MotorbikeDetailResponse getDetailById(Long id);
}