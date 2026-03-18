package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findByMotorbike(Motorbike motorbike);
    Review save(Review review);
}