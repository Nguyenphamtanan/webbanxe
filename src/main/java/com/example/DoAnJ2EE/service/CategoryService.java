package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.category.CategoryRequest;
import com.example.DoAnJ2EE.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAll();
    CategoryResponse getById(Integer id);
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Integer id, CategoryRequest request);
    void delete(Integer id);
}