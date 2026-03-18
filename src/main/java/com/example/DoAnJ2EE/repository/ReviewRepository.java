package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Review;
import com.example.DoAnJ2EE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMotorbike(Motorbike motorbike);
    Optional<Review> findByUserAndMotorbike(User user, Motorbike motorbike);
}