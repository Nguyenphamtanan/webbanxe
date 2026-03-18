package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotorbikeRepository extends JpaRepository<Motorbike, Long> {
    Optional<Motorbike> findBySlug(String slug);
    Optional<Motorbike> findBySku(String sku);

    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);
    long countByIsActiveTrue();

}