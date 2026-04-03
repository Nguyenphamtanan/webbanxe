package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.service.MotorbikeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShowroomController {

    private final MotorbikeService motorbikeService;

    public ShowroomController(MotorbikeService motorbikeService) {
        this.motorbikeService = motorbikeService;
    }

    @GetMapping("/showroom")
    public String showroomPage() {
        return "client/showroom";
    }

    @GetMapping("/san-pham/{slug}")
    public String showroomDetailPage(@PathVariable String slug, Model model) {
        MotorbikeDetailResponse bike = motorbikeService.getDetailBySlug(slug);
        model.addAttribute("bike", bike);
        return "client/motorbike-detail";
    }
}