package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.response.RegionResponse;
import com.example.DoAnJ2EE.entity.Region;
import com.example.DoAnJ2EE.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionApiController {

    private final RegionRepository regionRepository;

    @GetMapping
    public ResponseEntity<List<RegionResponse>> getAllRegions() {
        List<RegionResponse> regions = regionRepository.findAll()
                .stream()
                .map(region -> new RegionResponse(
                        region.getId(),
                        region.getName(),
                        region.getCode()
                ))
                .toList();

        return ResponseEntity.ok(regions);
    }
}