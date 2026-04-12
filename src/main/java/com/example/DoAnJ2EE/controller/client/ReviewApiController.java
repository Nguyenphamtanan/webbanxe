package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.review.ReviewRequest;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @PostMapping("/motorbike/{motorbikeId}")
    public ResponseEntity<?> createReview(@PathVariable Long motorbikeId,
                                          @Valid @RequestBody ReviewRequest request,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null || userDetails.getUser() == null) {
                return ResponseEntity.status(401).body("Bạn cần đăng nhập để đánh giá");
            }

            Long userId = userDetails.getUser().getId();
            reviewService.createReview(userId, motorbikeId, request);

            return ResponseEntity.ok("Đánh giá thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}