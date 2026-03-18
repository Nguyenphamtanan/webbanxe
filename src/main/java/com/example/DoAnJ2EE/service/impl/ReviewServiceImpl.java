package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Review;
import com.example.DoAnJ2EE.repository.ReviewRepository;
import com.example.DoAnJ2EE.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> findByMotorbike(Motorbike motorbike) {
        return reviewRepository.findByMotorbike(motorbike);
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
}