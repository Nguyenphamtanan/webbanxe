package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.category.CategoryRequest;
import com.example.DoAnJ2EE.dto.category.CategoryResponse;
import com.example.DoAnJ2EE.entity.Category;
import com.example.DoAnJ2EE.repository.CategoryRepository;
import com.example.DoAnJ2EE.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse getById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        return mapToResponse(category);
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }

        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug danh mục đã tồn tại");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .build();

        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        category.setName(request.getName());
        category.setSlug(request.getSlug());

        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        categoryRepository.delete(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}