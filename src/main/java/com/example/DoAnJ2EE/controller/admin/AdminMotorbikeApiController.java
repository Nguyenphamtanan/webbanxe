package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeRequest;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/motorbikes")
@RequiredArgsConstructor
public class AdminMotorbikeApiController {

    private final MotorbikeService motorbikeService;

    @GetMapping
    public List<MotorbikeResponse> getAll() {
        return motorbikeService.getAll();
    }

    @GetMapping("/{id}")
    public MotorbikeDetailResponse getById(@PathVariable Long id) {
        return motorbikeService.getById(id);
    }

    @PostMapping
    public MotorbikeResponse create(@RequestBody MotorbikeRequest request) {
        return motorbikeService.create(request);
    }

    @PutMapping("/{id}")
    public MotorbikeResponse update(@PathVariable Long id, @RequestBody MotorbikeRequest request) {
        return motorbikeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        motorbikeService.delete(id);
    }
}