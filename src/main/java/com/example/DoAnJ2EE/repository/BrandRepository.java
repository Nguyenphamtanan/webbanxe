package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> findBySlug(String slug);
    Optional<Brand> findByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}