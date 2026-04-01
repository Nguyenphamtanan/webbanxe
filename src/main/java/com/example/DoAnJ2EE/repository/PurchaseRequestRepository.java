package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.common.constant.PurchaseRequestStatus;
import com.example.DoAnJ2EE.entity.PurchaseRequest;
import com.example.DoAnJ2EE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    Optional<PurchaseRequest> findByRequestCode(String requestCode);
    List<PurchaseRequest> findByUserOrderByCreatedAtDesc(User user);
    List<PurchaseRequest> findByStatusOrderByCreatedAtDesc(PurchaseRequestStatus status);
    List<PurchaseRequest> findAllByOrderByCreatedAtDesc();
}