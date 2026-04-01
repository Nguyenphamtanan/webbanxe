package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.motorbike.MotorbikeDetailResponse;
import com.example.DoAnJ2EE.service.ShowroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PurchaseRequestPageController {

    private final ShowroomService showroomService;

    @GetMapping("/customer/requests/quote")
    public String quoteRequestPage(@RequestParam("motorbikeId") Long motorbikeId, Model model) {
        MotorbikeDetailResponse bike = showroomService.getDetailById(motorbikeId);
        model.addAttribute("bike", bike);
        return "client/quote-request";
    }
}