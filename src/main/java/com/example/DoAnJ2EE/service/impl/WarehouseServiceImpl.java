package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.warehouse.WarehouseResponse;
import com.example.DoAnJ2EE.repository.WarehouseRepository;
import com.example.DoAnJ2EE.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAll().stream()
                .map(w -> WarehouseResponse.builder()
                        .id(w.getId())
                        .name(w.getName())
                        .address(w.getAddress())
                        .build())
                .toList();
    }
}