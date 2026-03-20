package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encoded);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/v1/oauth2/token",
                HttpMethod.POST,
                entity,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    @Override
    public String createOrder(String accessToken, String amount, String currency) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        JSONObject body = new JSONObject();
        body.put("intent", "CAPTURE");

        JSONObject amountObj = new JSONObject();
        amountObj.put("currency_code", currency);
        amountObj.put("value", amount);

        JSONObject purchaseUnit = new JSONObject();
        purchaseUnit.put("amount", amountObj);

        body.put("purchase_units", new JSONArray().put(purchaseUnit));

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    @Override
    public void captureOrder(String accessToken, String paypalOrderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        restTemplate.exchange(
                baseUrl + "/v2/checkout/orders/" + paypalOrderId + "/capture",
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
