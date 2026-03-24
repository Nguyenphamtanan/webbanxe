package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Stock;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.repository.StockRepository;
import com.example.DoAnJ2EE.service.ShowroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowroomServiceImpl implements ShowroomService {

    private final StockRepository stockRepository;
    private final MotorbikeRepository motorbikeRepository;

    @Override
    public List<MotorbikeResponse> filter(Integer warehouseId, Integer categoryId, Integer brandId) {
        return stockRepository.filterStocks(warehouseId, categoryId, brandId)
                .stream()
                .map(Stock::getMotorbike)
                .distinct()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MotorbikeDetailResponse getDetailBySlug(String slug) {
        Motorbike motorbike = motorbikeRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        return MotorbikeDetailResponse.builder()
                .id(motorbike.getId())
                .name(motorbike.getName())
                .slug(motorbike.getSlug())
                .sku(motorbike.getSku())
                .price(motorbike.getPrice())
                .description(motorbike.getDescription())
                .engine(motorbike.getEngine())
                .weightKg(motorbike.getWeightKg())
                .primaryImageUrl(motorbike.getPrimaryImageUrl())
                .brandName(motorbike.getBrand().getName())
                .categoryName(motorbike.getCategory().getName())
                .isActive(motorbike.getIsActive())
                .build();
    }

    private MotorbikeResponse mapToResponse(Motorbike motorbike) {
        return MotorbikeResponse.builder()
                .id(motorbike.getId())
                .name(motorbike.getName())
                .slug(motorbike.getSlug())
                .sku(motorbike.getSku())
                .price(motorbike.getPrice())
                .primaryImageUrl(motorbike.getPrimaryImageUrl())
                .brandName(motorbike.getBrand().getName())
                .categoryName(motorbike.getCategory().getName())
                .isActive(motorbike.getIsActive())
                .build();
    }
}