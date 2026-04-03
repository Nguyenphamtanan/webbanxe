package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeRequest;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.entity.Brand;
import com.example.DoAnJ2EE.entity.Category;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.repository.BrandRepository;
import com.example.DoAnJ2EE.repository.CategoryRepository;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotorbikeServiceImpl implements MotorbikeService {

    private final MotorbikeRepository motorbikeRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<MotorbikeResponse> getAll() {
        return motorbikeRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MotorbikeDetailResponse getById(Long id) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        return mapToDetailResponse(motorbike);
    }

    @Override
    public MotorbikeDetailResponse getBySlug(String slug) {
        Motorbike motorbike = motorbikeRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        return mapToDetailResponse(motorbike);
    }

    @Override
    public MotorbikeResponse create(MotorbikeRequest request) {
        if (motorbikeRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại");
        }

        if (motorbikeRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU đã tồn tại");
        }

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy brand"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

        Motorbike motorbike = Motorbike.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .sku(request.getSku())
                .price(request.getPrice())
                .description(request.getDescription())
                .engine(request.getEngine())
                .weightKg(request.getWeightKg())
                .primaryImageUrl(request.getPrimaryImageUrl())
                .brand(brand)
                .category(category)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        return mapToResponse(motorbikeRepository.save(motorbike));
    }

    @Override
    public MotorbikeResponse update(Long id, MotorbikeRequest request) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy brand"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

        motorbike.setName(request.getName());
        motorbike.setSlug(request.getSlug());
        motorbike.setSku(request.getSku());
        motorbike.setPrice(request.getPrice());
        motorbike.setDescription(request.getDescription());
        motorbike.setEngine(request.getEngine());
        motorbike.setWeightKg(request.getWeightKg());
        motorbike.setPrimaryImageUrl(request.getPrimaryImageUrl());
        motorbike.setBrand(brand);
        motorbike.setCategory(category);
        motorbike.setIsActive(request.getIsActive());

        return mapToResponse(motorbikeRepository.save(motorbike));
    }

    @Override
    public void delete(Long id) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        motorbikeRepository.delete(motorbike);
    }
    @Override
    public MotorbikeDetailResponse getDetailBySlug(String slug) {
        Motorbike motorbike = motorbikeRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với slug: " + slug));

        return mapToDetailResponse(motorbike);
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

    private MotorbikeDetailResponse mapToDetailResponse(Motorbike motorbike) {
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
                .brandId(motorbike.getBrand() != null ? motorbike.getBrand().getId() : null)
                .brandName(motorbike.getBrand() != null ? motorbike.getBrand().getName() : null)
                .categoryId(motorbike.getCategory() != null ? motorbike.getCategory().getId() : null)
                .categoryName(motorbike.getCategory() != null ? motorbike.getCategory().getName() : null)
                .isActive(motorbike.getIsActive())
                .build();
    }
}