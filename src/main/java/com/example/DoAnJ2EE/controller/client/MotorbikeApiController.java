package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motorbikes")
@RequiredArgsConstructor
public class MotorbikeApiController {

    private final MotorbikeService motorbikeService;

    @GetMapping
    public List<MotorbikeResponse> getAll() {
        return motorbikeService.getAll();
    }

    @GetMapping("/{slug}")
    public MotorbikeDetailResponse getBySlug(@PathVariable String slug) {
        return motorbikeService.getBySlug(slug);
    }
}