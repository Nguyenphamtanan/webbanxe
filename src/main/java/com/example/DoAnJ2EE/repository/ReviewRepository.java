package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMotorbikeIdOrderByCreatedAtDesc(Long motorbikeId);

    Optional<Review> findByUserIdAndMotorbikeId(Long userId, Long motorbikeId);

    boolean existsByUserIdAndMotorbikeId(Long userId, Long motorbikeId);

    long countByMotorbikeId(Long motorbikeId);

    @Query("""
        select avg(r.rating)
        from Review r
        where r.motorbike.id = :motorbikeId
    """)
    Double getAverageRatingByMotorbikeId(Long motorbikeId);
}