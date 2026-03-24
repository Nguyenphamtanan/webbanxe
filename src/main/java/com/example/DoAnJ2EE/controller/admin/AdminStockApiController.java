package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.stock.StockRequest;
import com.example.DoAnJ2EE.dto.stock.StockResponse;
import com.example.DoAnJ2EE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stocks")
@RequiredArgsConstructor
public class AdminStockApiController {

    private final StockService stockService;

    @GetMapping
    public List<StockResponse> getAll() {
        return stockService.getAll();
    }

    @PostMapping
    public StockResponse create(@RequestBody StockRequest request) {
        return stockService.create(request);
    }

    @PutMapping("/{id}")
    public StockResponse update(@PathVariable Long id,
                                @RequestBody StockRequest request) {
        return stockService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        stockService.delete(id);
        return "Xóa stock thành công";
    }
}