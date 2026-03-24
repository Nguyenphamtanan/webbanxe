package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.service.ShowroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MotorbikeDetailPageController {

    private final ShowroomService showroomService;

    @GetMapping("/showroom/{slug}")
    public String detailPage(@PathVariable String slug, Model model) {
        model.addAttribute("bike", showroomService.getDetailBySlug(slug));
        return "client/motorbike-detail";
    }
}