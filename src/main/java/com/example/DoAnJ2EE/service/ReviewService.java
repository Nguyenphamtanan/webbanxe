package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.review.ReviewRequest;
import com.example.DoAnJ2EE.entity.Review;

import java.util.List;

public interface ReviewService {
    void createReview(Long userId, Long motorbikeId, ReviewRequest request);
    boolean canUserReview(Long userId, Long motorbikeId);
    List<Review> getReviewsByMotorbike(Long motorbikeId);
    Double getAverageRating(Long motorbikeId);
    long getReviewCount(Long motorbikeId);
}