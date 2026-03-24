package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.warehouse.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    List<WarehouseResponse> getAll();
}