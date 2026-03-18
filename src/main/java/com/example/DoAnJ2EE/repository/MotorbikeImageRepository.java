package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.MotorbikeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotorbikeImageRepository extends JpaRepository<MotorbikeImage, Long> {
    List<MotorbikeImage> findByMotorbikeOrderBySortOrderAsc(Motorbike motorbike);
}