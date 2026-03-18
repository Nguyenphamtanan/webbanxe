package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.service.MotorbikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MotorbikeService motorbikeService;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("motorbikes", motorbikeService.getAll());

        return "client/home";
    }
}