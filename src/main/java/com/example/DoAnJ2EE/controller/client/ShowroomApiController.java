package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.service.ShowroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showroom")
@RequiredArgsConstructor
public class ShowroomApiController {

    private final ShowroomService showroomService;

    @GetMapping("/filter")
    public List<MotorbikeResponse> filter(
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer brandId
    ) {
        return showroomService.filter(warehouseId, categoryId, brandId);
    }

    @GetMapping("/{slug}")
    public MotorbikeDetailResponse detail(@PathVariable String slug) {
        return showroomService.getDetailBySlug(slug);
    }
}