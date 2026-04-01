package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.dto.motorbike.MotorbikeResponse;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Stock;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.repository.StockRepository;
import com.example.DoAnJ2EE.service.ShowroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ShowroomServiceImpl implements ShowroomService {

    private final StockRepository stockRepository;
    private final MotorbikeRepository motorbikeRepository;

    @Override
    public List<MotorbikeResponse> filter(Integer warehouseId,
                                          Integer categoryId,
                                          Integer brandId,
                                          String keyword,
                                          String sortBy,
                                          String sortDir) {

        return stockRepository.filterStocks(warehouseId, categoryId, brandId)
                .stream()
                .map(Stock::getMotorbike)
                .filter(motorbike -> Boolean.TRUE.equals(motorbike.getIsActive()))
                .distinct()
                .filter(motorbike -> matchesKeyword(motorbike, keyword))
                .sorted(buildComparator(sortBy, sortDir))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MotorbikeDetailResponse getDetailBySlug(String slug) {
        Motorbike motorbike = motorbikeRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        return mapToDetailResponse(motorbike);
    }

    @Override
    public List<MotorbikeDetailResponse> getCompareItems(List<Long> ids) {
        return motorbikeRepository.findAllById(ids)
                .stream()
                .filter(motorbike -> Boolean.TRUE.equals(motorbike.getIsActive()))
                .map(this::mapToDetailResponse)
                .toList();
    }

    private boolean matchesKeyword(Motorbike motorbike, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        String name = motorbike.getName() == null ? "" : motorbike.getName().toLowerCase(Locale.ROOT);

        return name.contains(normalizedKeyword);
    }

    private Comparator<Motorbike> buildComparator(String sortBy, String sortDir) {
        Comparator<Motorbike> comparator;

        if ("price".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(
                    Motorbike::getPrice,
                    Comparator.nullsLast(BigDecimal::compareTo)
            );
        } else if ("name".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(
                    motorbike -> motorbike.getName() == null ? "" : motorbike.getName().toLowerCase(Locale.ROOT)
            );
        } else {
            comparator = Comparator.comparing(
                    Motorbike::getId,
                    Comparator.nullsLast(Long::compareTo)
            );
        }

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private MotorbikeResponse mapToResponse(Motorbike motorbike) {
        return MotorbikeResponse.builder()
                .id(motorbike.getId())
                .name(motorbike.getName())
                .slug(motorbike.getSlug())
                .sku(motorbike.getSku())
                .price(motorbike.getPrice())
                .primaryImageUrl(motorbike.getPrimaryImageUrl())
                .brandName(motorbike.getBrand() != null ? motorbike.getBrand().getName() : null)
                .categoryName(motorbike.getCategory() != null ? motorbike.getCategory().getName() : null)
                .isActive(motorbike.getIsActive())
                .build();
    }

    private MotorbikeDetailResponse mapToDetailResponse(Motorbike motorbike) {
        return MotorbikeDetailResponse.builder()
                .id(motorbike.getId())
                .name(motorbike.getName())
                .slug(motorbike.getSlug())
                .sku(motorbike.getSku())
                .price(motorbike.getPrice())
                .description(motorbike.getDescription())
                .engine(motorbike.getEngine())
                .weightKg(motorbike.getWeightKg())
                .primaryImageUrl(motorbike.getPrimaryImageUrl())
                .brandName(motorbike.getBrand() != null ? motorbike.getBrand().getName() : null)
                .categoryName(motorbike.getCategory() != null ? motorbike.getCategory().getName() : null)
                .isActive(motorbike.getIsActive())
                .build();
    }
    @Override
    public MotorbikeDetailResponse getDetailById(Long id) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        return mapToDetailResponse(motorbike);
    }
}