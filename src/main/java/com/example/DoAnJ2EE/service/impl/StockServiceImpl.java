package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.stock.StockRequest;
import com.example.DoAnJ2EE.dto.stock.StockResponse;
import com.example.DoAnJ2EE.entity.*;
import com.example.DoAnJ2EE.repository.*;
import com.example.DoAnJ2EE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final WarehouseRepository warehouseRepository;
    private final MotorbikeRepository motorbikeRepository;

    @Override
    public List<StockResponse> getAll() {
        return stockRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public StockResponse create(StockRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho"));

        Motorbike motorbike = motorbikeRepository.findById(request.getMotorbikeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        Stock stock = Stock.builder()
                .warehouse(warehouse)
                .motorbike(motorbike)
                .quantity(request.getQuantity())
                .build();

        return mapToResponse(stockRepository.save(stock));
    }

    @Override
    public StockResponse update(Long id, StockRequest request) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy stock"));

        stock.setQuantity(request.getQuantity());

        return mapToResponse(stockRepository.save(stock));
    }

    @Override
    public void delete(Long id) {
        stockRepository.deleteById(id);
    }

    private StockResponse mapToResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .warehouseName(stock.getWarehouse().getName())
                .motorbikeName(stock.getMotorbike().getName())
                .quantity(stock.getQuantity())
                .build();
    }
}