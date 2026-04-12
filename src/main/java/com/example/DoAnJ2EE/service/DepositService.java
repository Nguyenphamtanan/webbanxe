package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.request.CreateDepositRequest;
import com.example.DoAnJ2EE.dto.response.DepositResponse;
import com.example.DoAnJ2EE.entity.User;

import java.util.List;

public interface DepositService {
    DepositResponse create(User user, CreateDepositRequest request);
    DepositResponse markPaid(Long id, User user);
    DepositResponse getById(Long id, User user);
    List<DepositResponse> getMyDeposits(User user);

    List<DepositResponse> getAllForAdmin();
    DepositResponse adminConfirm(Long id);
    DepositResponse getAdminById(Long id);
    DepositResponse updateStatusByAdmin(Long id, String status, String note);
}