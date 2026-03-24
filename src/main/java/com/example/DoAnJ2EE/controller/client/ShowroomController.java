package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.service.BrandService;
import com.example.DoAnJ2EE.service.CategoryService;
import com.example.DoAnJ2EE.service.ShowroomService;
import com.example.DoAnJ2EE.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ShowroomController {

    private final WarehouseService warehouseService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping("/showroom")
    public String showroomPage(Model model) {
        model.addAttribute("warehouses", warehouseService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("brands", brandService.getAll());
        return "client/showroom";
    }
}