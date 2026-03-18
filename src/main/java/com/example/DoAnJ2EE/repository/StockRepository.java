package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Stock;
import com.example.DoAnJ2EE.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByWarehouseAndMotorbike(Warehouse warehouse, Motorbike motorbike);
    List<Stock> findByMotorbike(Motorbike motorbike);
    List<Stock> findByWarehouse(Warehouse warehouse);
}