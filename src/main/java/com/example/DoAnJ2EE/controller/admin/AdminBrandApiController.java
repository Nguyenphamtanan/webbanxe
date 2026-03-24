package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.brand.BrandRequest;
import com.example.DoAnJ2EE.dto.brand.BrandResponse;
import com.example.DoAnJ2EE.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/brands")
@RequiredArgsConstructor
public class AdminBrandApiController {

    private final BrandService brandService;

    @GetMapping
    public List<BrandResponse> getAll() {
        return brandService.getAll();
    }

    @GetMapping("/{id}")
    public BrandResponse getById(@PathVariable Integer id) {
        return brandService.getById(id);
    }

    @PostMapping
    public BrandResponse create(@RequestBody BrandRequest request) {
        return brandService.create(request);
    }

    @PutMapping("/{id}")
    public BrandResponse update(@PathVariable Integer id,
                                @RequestBody BrandRequest request) {
        return brandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        brandService.delete(id);
        return "Xóa hãng thành công";
    }
}