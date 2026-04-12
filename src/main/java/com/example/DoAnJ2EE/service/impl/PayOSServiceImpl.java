package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.config.PayOSProperties;
import com.example.DoAnJ2EE.dto.response.PayOSCreateResponse;
import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.service.PayOSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    private final PayOSProperties payOSProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PayOSCreateResponse createDepositPayment(Deposit deposit) {
        try {
            System.out.println("payos clientId = " + payOSProperties.getClientId());
            System.out.println("payos returnUrl config = " + payOSProperties.getReturnUrl());
            System.out.println("payos cancelUrl config = " + payOSProperties.getCancelUrl());

            PayOS client = new PayOS(
                    payOSProperties.getClientId(),
                    payOSProperties.getApiKey(),
                    payOSProperties.getChecksumKey()
            );

            long orderCode = System.currentTimeMillis();

            String returnUrl = payOSProperties.getReturnUrl() + "?depositId=" + deposit.getId();
            String cancelUrl = payOSProperties.getCancelUrl() + "?depositId=" + deposit.getId();

            System.out.println("PAYOS returnUrl = " + returnUrl);
            System.out.println("PAYOS cancelUrl = " + cancelUrl);

            PaymentLinkItem item = PaymentLinkItem.builder()
                    .name("Dat coc " + deposit.getMotorbike().getName())
                    .quantity(1)
                    .price(deposit.getDepositAmount().longValue())
                    .build();

            CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount(deposit.getDepositAmount().longValue())
                    .description("Dat coc " + deposit.getDepositCode())
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
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

    @Override
    public void createRefundPayout(String referenceId,
                                   Long amount,
                                   String description,
                                   String toBin,
                                   String toAccountNumber) {
        try {
            String url = payOSProperties.getPayoutUrl();

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("referenceId", referenceId);
            body.put("amount", amount);
            body.put("description", description);
            body.put("toBin", toBin);
            body.put("toAccountNumber", toAccountNumber);

            String signature = signPayout(
                    referenceId,
                    amount,
                    description,
                    toBin,
                    toAccountNumber,
                    payOSProperties.getPayoutChecksumKey()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-client-id", payOSProperties.getPayoutClientId());
            headers.set("x-api-key", payOSProperties.getPayoutApiKey());
            headers.set("x-idempotency-key", UUID.randomUUID().toString());
            headers.set("x-signature", signature);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("PAYOS refund referenceId = " + referenceId);
            System.out.println("PAYOS refund status = " + response.getStatusCode());
            System.out.println("PAYOS refund response = " + response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("PayOS payout không thành công");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo lệnh hoàn tiền PayOS: " + e.getMessage(), e);
        }
    }

    private String signPayout(String referenceId,
                              Long amount,
                              String description,
                              String toBin,
                              String toAccountNumber,
                              String checksumKey) {
        try {
            String rawData =
                    "amount=" + amount
                            + "&description=" + description
                            + "&referenceId=" + referenceId
                            + "&toAccountNumber=" + toAccountNumber
                            + "&toBin=" + toBin;

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    checksumKey.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            sha256Hmac.init(secretKey);

            byte[] hash = sha256Hmac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký payout: " + e.getMessage(), e);
        }
    }
}