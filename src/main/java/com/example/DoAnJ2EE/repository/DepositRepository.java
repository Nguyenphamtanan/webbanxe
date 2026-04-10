package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.entity.PurchaseRequest;
import com.example.DoAnJ2EE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findByUserOrderByCreatedAtDesc(User user);
    Optional<Deposit> findByPurchaseRequest(PurchaseRequest purchaseRequest);
    List<Deposit> findAllByOrderByCreatedAtDesc();
    Optional<Deposit> findByPayosOrderCode(Long payosOrderCode);
}