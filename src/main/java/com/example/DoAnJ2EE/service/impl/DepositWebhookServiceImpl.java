package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.DepositStatus;
import com.example.DoAnJ2EE.common.constant.PaymentStatus;
import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.repository.DepositRepository;
import com.example.DoAnJ2EE.service.DepositWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DepositWebhookServiceImpl implements DepositWebhookService {

    private final DepositRepository depositRepository;

    @Override
    @SuppressWarnings("unchecked")
    public void handleWebhook(Map<String, Object> payload) {
        Object success = payload.get("success");
        if (!(success instanceof Boolean) || !((Boolean) success)) {
            return;
        }

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if (data == null) {
            return;
        }

        Number orderCodeNumber = (Number) data.get("orderCode");
        if (orderCodeNumber == null) {
            return;
        }

        Long orderCode = orderCodeNumber.longValue();

        Deposit deposit = depositRepository.findByPayosOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy deposit theo payosOrderCode"));

        if (deposit.getPaymentStatus() == PaymentStatus.PAID) {
            return;
        }

        deposit.setPaymentStatus(PaymentStatus.PAID);
        deposit.setStatus(DepositStatus.PAID);
        deposit.setPaidAt(LocalDateTime.now());

        Object reference = data.get("reference");
        if (reference != null) {
            deposit.setTransactionCode(String.valueOf(reference));
        }

        depositRepository.save(deposit);
    }
}