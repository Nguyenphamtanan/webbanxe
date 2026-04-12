package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.OrderStatus;
import com.example.DoAnJ2EE.dto.review.ReviewRequest;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.Review;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.repository.OrderRepository;
import com.example.DoAnJ2EE.repository.ReviewRepository;
import com.example.DoAnJ2EE.repository.UserRepository;
import com.example.DoAnJ2EE.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MotorbikeRepository motorbikeRepository;

    @Override
    @Transactional
    public void createReview(Long userId, Long motorbikeId, ReviewRequest request) {
        if (reviewRepository.existsByUserIdAndMotorbikeId(userId, motorbikeId)) {
            throw new IllegalStateException("Bạn đã đánh giá xe này rồi");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user"));

        Motorbike motorbike = motorbikeRepository.findById(motorbikeId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy xe"));

        boolean bought = !orderRepository.findByUserAndStatus(user, OrderStatus.DELIVERED).isEmpty();

        if (!bought) {
            throw new IllegalStateException("Bạn phải có đơn hàng đã giao mới được đánh giá");
        }

        Review review = Review.builder()
                .user(user)
                .motorbike(motorbike)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);
    }

    @Override
    public boolean canUserReview(Long userId, Long motorbikeId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return false;
        }

        boolean bought = !orderRepository.findByUserAndStatus(user, OrderStatus.DELIVERED).isEmpty();
        boolean reviewed = reviewRepository.existsByUserIdAndMotorbikeId(userId, motorbikeId);

        return bought && !reviewed;
    }
    @Override
    public List<Review> getReviewsByMotorbike(Long motorbikeId) {
        return reviewRepository.findByMotorbikeIdOrderByCreatedAtDesc(motorbikeId);
    }

    @Override
    public Double getAverageRating(Long motorbikeId) {
        Double avg = reviewRepository.getAverageRatingByMotorbikeId(motorbikeId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public long getReviewCount(Long motorbikeId) {
        return reviewRepository.countByMotorbikeId(motorbikeId);
    }
}