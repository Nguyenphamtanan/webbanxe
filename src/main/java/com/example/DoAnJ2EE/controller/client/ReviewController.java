package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.review.ReviewRequest;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews/motorbike/{motorbikeId}")
    public String createReview(@PathVariable Long motorbikeId,
                               @Valid ReviewRequest request,
                               @RequestParam String slug,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            if (userDetails == null || userDetails.getUser() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn cần đăng nhập để đánh giá");
                return "redirect:/login";
            }

            Long userId = userDetails.getUser().getId();
            reviewService.createReview(userId, motorbikeId, request);
            redirectAttributes.addFlashAttribute("successMessage", "Đánh giá thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/showroom/" + slug;
    }
}