package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.PurchaseRequest;
import com.example.DoAnJ2EE.entity.PurchaseRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRequestItemRepository extends JpaRepository<PurchaseRequestItem, Long> {
    List<PurchaseRequestItem> findByPurchaseRequest(PurchaseRequest purchaseRequest);
}