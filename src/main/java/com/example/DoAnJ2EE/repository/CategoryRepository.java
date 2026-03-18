package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findBySlug(String slug);
    Optional<Category> findByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}