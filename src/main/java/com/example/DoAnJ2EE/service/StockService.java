package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.stock.StockRequest;
import com.example.DoAnJ2EE.dto.stock.StockResponse;

import java.util.List;

public interface StockService {
    List<StockResponse> getAll();
    StockResponse create(StockRequest request);
    StockResponse update(Long id, StockRequest request);
    void delete(Long id);
}