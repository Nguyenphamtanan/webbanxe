package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.brand.BrandRequest;
import com.example.DoAnJ2EE.dto.brand.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getAll();
    BrandResponse getById(Integer id);
    BrandResponse create(BrandRequest request);
    BrandResponse update(Integer id, BrandRequest request);
    void delete(Integer id);
}