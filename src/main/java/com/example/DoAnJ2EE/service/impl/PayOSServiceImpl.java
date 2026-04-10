package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.config.PayOSProperties;
import com.example.DoAnJ2EE.dto.response.PayOSCreateResponse;
import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.service.PayOSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    private final PayOSProperties payOSProperties;

    @Override
    public PayOSCreateResponse createDepositPayment(Deposit deposit) {
        try {
            System.out.println("payos clientId = " + payOSProperties.getClientId());
            System.out.println("payos returnUrl = " + payOSProperties.getReturnUrl());
            System.out.println("payos cancelUrl = " + payOSProperties.getCancelUrl());

            PayOS client = new PayOS(
                    payOSProperties.getClientId(),
                    payOSProperties.getApiKey(),
                    payOSProperties.getChecksumKey()
            );

            long orderCode = System.currentTimeMillis();

            PaymentLinkItem item = PaymentLinkItem.builder()
                    .name("Dat coc " + deposit.getMotorbike().getName())
                    .quantity(1)
                    .price(deposit.getDepositAmount().longValue())
                    .build();

            CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount(deposit.getDepositAmount().longValue())
                    .description("Dat coc " + deposit.getDepositCode())
                    .returnUrl(payOSProperties.getReturnUrl() + "?depositId=" + deposit.getId())
                    .cancelUrl(payOSProperties.getCancelUrl() + "?depositId=" + deposit.getId())
                    .item(item)
                    .build();

            CreatePaymentLinkResponse response = client.paymentRequests().create(request);

            System.out.println("PAYOS orderCode = " + orderCode);
            System.out.println("PAYOS checkoutUrl = " + response.getCheckoutUrl());
            System.out.println("PAYOS qrCode = " + response.getQrCode());

            return new PayOSCreateResponse(
                    orderCode,
                    response.getCheckoutUrl(),
                    response.getQrCode()
            );
        } catch (PayOSException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo link thanh toán PayOS: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi không xác định khi tạo thanh toán PayOS: " + e.getMessage(), e);
        }
    }
}