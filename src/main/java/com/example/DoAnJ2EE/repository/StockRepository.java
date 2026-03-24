package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Stock;
import com.example.DoAnJ2EE.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByWarehouseAndMotorbike(Warehouse warehouse, Motorbike motorbike);
    List<Stock> findByMotorbike(Motorbike motorbike);
    List<Stock> findByWarehouse(Warehouse warehouse);
    @Query("""
        select s from Stock s
        where (:warehouseId is null or s.warehouse.id = :warehouseId)
          and (:categoryId is null or s.motorbike.category.id = :categoryId)
          and (:brandId is null or s.motorbike.brand.id = :brandId)
          and s.quantity > 0
    """)
    List<Stock> filterStocks(Integer warehouseId, Integer categoryId, Integer brandId);
}