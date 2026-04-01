package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.request.CreatePurchaseRequestRequest;
import com.example.DoAnJ2EE.dto.request.QuoteRequestUpdateRequest;
import com.example.DoAnJ2EE.dto.request.UpdatePurchaseRequestStatusRequest;
import com.example.DoAnJ2EE.dto.response.AdminPurchaseRequestResponse;
import com.example.DoAnJ2EE.dto.response.MyPurchaseRequestResponse;
import com.example.DoAnJ2EE.dto.response.PurchaseRequestResponse;
import com.example.DoAnJ2EE.entity.User;

import java.util.List;

public interface PurchaseRequestService {
    PurchaseRequestResponse createFromCart(User user, CreatePurchaseRequestRequest request);
    PurchaseRequestResponse createQuoteRequest(User user, CreatePurchaseRequestRequest request);
    List<MyPurchaseRequestResponse> getMyRequests(User user);
    List<AdminPurchaseRequestResponse> getAllForAdmin();
    PurchaseRequestResponse getById(Long id);
    PurchaseRequestResponse updateStatus(Long id, UpdatePurchaseRequestStatusRequest request);
    PurchaseRequestResponse updateQuote(Long id, QuoteRequestUpdateRequest request);
    PurchaseRequestResponse customerDecision(Long id, User user, UpdatePurchaseRequestStatusRequest request);
}