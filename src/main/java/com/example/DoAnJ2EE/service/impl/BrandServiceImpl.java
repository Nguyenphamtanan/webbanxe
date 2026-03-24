package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.brand.BrandRequest;
import com.example.DoAnJ2EE.dto.brand.BrandResponse;
import com.example.DoAnJ2EE.entity.Brand;
import com.example.DoAnJ2EE.repository.BrandRepository;
import com.example.DoAnJ2EE.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public List<BrandResponse> getAll() {
        return brandRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BrandResponse getById(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hãng"));
        return mapToResponse(brand);
    }

    @Override
    public BrandResponse create(BrandRequest request) {
        if (brandRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên hãng đã tồn tại");
        }

        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug hãng đã tồn tại");
        }

        Brand brand = Brand.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .build();

        return mapToResponse(brandRepository.save(brand));
    }

    @Override
    public BrandResponse update(Integer id, BrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hãng"));

        brand.setName(request.getName());
        brand.setSlug(request.getSlug());

        return mapToResponse(brandRepository.save(brand));
    }

    @Override
    public void delete(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hãng"));

        brandRepository.delete(brand);
    }

    private BrandResponse mapToResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .build();
    }
}