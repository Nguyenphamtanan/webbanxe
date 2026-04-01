package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.service.ShowroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showroom")
@RequiredArgsConstructor
public class ShowroomApiController {

    private final ShowroomService showroomService;

    @GetMapping("/filter")
    public ResponseEntity<List<MotorbikeResponse>> filter(
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(
                showroomService.filter(warehouseId, categoryId, brandId, keyword, sortBy, sortDir)
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<MotorbikeDetailResponse> detail(@PathVariable String slug) {
        return ResponseEntity.ok(showroomService.getDetailBySlug(slug));
    }

    @PostMapping("/compare")
    public ResponseEntity<List<MotorbikeDetailResponse>> compare(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(showroomService.getCompareItems(ids));
    }
}