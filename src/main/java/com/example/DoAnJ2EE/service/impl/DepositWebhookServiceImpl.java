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
        System.out.println("=== HANDLE WEBHOOK START ===");
        System.out.println("payload = " + payload);

        Map<String, Object> data = null;

        Object dataObj = payload.get("data");
        if (dataObj instanceof Map) {
            data = (Map<String, Object>) dataObj;
        }

        if (data == null) {
            throw new RuntimeException("Webhook không có trường data");
        }

        Object orderCodeObj = data.get("orderCode");
        if (orderCodeObj == null) {
            throw new RuntimeException("Webhook không có orderCode");
        }

        Long orderCode;
        if (orderCodeObj instanceof Number) {
            orderCode = ((Number) orderCodeObj).longValue();
        } else {
            orderCode = Long.valueOf(String.valueOf(orderCodeObj));
        }

        System.out.println("orderCode = " + orderCode);

        Deposit deposit = depositRepository.findByPayosOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy deposit theo payosOrderCode: " + orderCode));

        System.out.println("deposit id = " + deposit.getId());
        System.out.println("old paymentStatus = " + deposit.getPaymentStatus());
        System.out.println("old status = " + deposit.getStatus());

        if (deposit.getPaymentStatus() == PaymentStatus.PAID) {
            System.out.println("Deposit đã PAID trước đó, bỏ qua");
            return;
        }

        deposit.setPaymentStatus(PaymentStatus.PAID);
        deposit.setStatus(DepositStatus.PAID);
        deposit.setPaidAt(LocalDateTime.now());

        Object reference = data.get("reference");
        if (reference == null) {
            reference = data.get("transactionCode");
        }
        if (reference == null) {
            reference = data.get("paymentLinkId");
        }
        if (reference != null) {
            deposit.setTransactionCode(String.valueOf(reference));
        }

        depositRepository.save(deposit);

        System.out.println("=== HANDLE WEBHOOK SUCCESS ===");
    }
}