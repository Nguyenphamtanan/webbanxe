package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.service.BrandService;
import com.example.DoAnJ2EE.service.CategoryService;
import com.example.DoAnJ2EE.service.MotorbikeService;
import com.example.DoAnJ2EE.service.ReviewService;
import com.example.DoAnJ2EE.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShowroomController {

    private final MotorbikeService motorbikeService;
    private final WarehouseService warehouseService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ReviewService reviewService;

    public ShowroomController(MotorbikeService motorbikeService,
                              WarehouseService warehouseService,
                              CategoryService categoryService,
                              BrandService brandService,
                              ReviewService reviewService) {
        this.motorbikeService = motorbikeService;
        this.warehouseService = warehouseService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.reviewService = reviewService;
    }

    @GetMapping("/showroom")
    public String showroomPage(Model model) {
        model.addAttribute("warehouses", warehouseService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("brands", brandService.getAll());
        return "client/showroom";
    }

    @GetMapping("/san-pham/{slug}")
    public String showroomDetailPage(@PathVariable String slug, Model model) {
        MotorbikeDetailResponse bike = motorbikeService.getDetailBySlug(slug);

        model.addAttribute("bike", bike);
        model.addAttribute("reviews", reviewService.getReviewsByMotorbike(bike.getId()));
        model.addAttribute("averageRating", reviewService.getAverageRating(bike.getId()));
        model.addAttribute("reviewCount", reviewService.getReviewCount(bike.getId()));

        return "client/motorbike-detail";
    }
}