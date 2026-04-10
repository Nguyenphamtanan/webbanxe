package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.response.PayOSCreateResponse;
import com.example.DoAnJ2EE.entity.Deposit;

public interface PayOSService {
    PayOSCreateResponse createDepositPayment(Deposit deposit);
}