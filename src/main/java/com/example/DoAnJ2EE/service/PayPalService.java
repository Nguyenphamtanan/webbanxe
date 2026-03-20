package com.example.DoAnJ2EE.service;

public interface PayPalService {
    String getAccessToken();
    String createOrder(String accessToken, String amount, String currency);
    void captureOrder(String accessToken, String paypalOrderId);
}