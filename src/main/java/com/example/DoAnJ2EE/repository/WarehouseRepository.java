package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}