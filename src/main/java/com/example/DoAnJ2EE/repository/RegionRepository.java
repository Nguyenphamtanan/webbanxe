package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByCode(String code);
    boolean existsByCode(String code);
}