package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.DepositStatus;
import com.example.DoAnJ2EE.common.constant.PaymentMethod;
import com.example.DoAnJ2EE.common.constant.PaymentStatus;
import com.example.DoAnJ2EE.common.constant.PurchaseRequestStatus;
import com.example.DoAnJ2EE.dto.request.CreateDepositRequest;
import com.example.DoAnJ2EE.dto.response.DepositResponse;
import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.entity.PurchaseRequest;
import com.example.DoAnJ2EE.entity.PurchaseRequestItem;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.DepositRepository;
import com.example.DoAnJ2EE.repository.PurchaseRequestItemRepository;
import com.example.DoAnJ2EE.repository.PurchaseRequestRepository;
import com.example.DoAnJ2EE.service.DepositService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestItemRepository purchaseRequestItemRepository;

    @Override
    @Transactional
    public DepositResponse create(User user, CreateDepositRequest request) {
        if (request.getRequestId() == null) {
            throw new RuntimeException("Thiếu requestId");
        }

        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (purchaseRequest.getUser() == null || !purchaseRequest.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền tạo đặt cọc cho yêu cầu này");
        }

        if (purchaseRequest.getStatus() != PurchaseRequestStatus.APPROVED) {
            throw new RuntimeException("Chỉ được đặt cọc khi yêu cầu đã được đồng ý");
        }

        depositRepository.findByPurchaseRequest(purchaseRequest).ifPresent(d -> {
            throw new RuntimeException("Yêu cầu này đã có đặt cọc");
        });

        PurchaseRequestItem item = purchaseRequestItemRepository.findByPurchaseRequest(purchaseRequest)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe trong yêu cầu"));

        BigDecimal quotedPrice = purchaseRequest.getQuotedPrice();
        if (quotedPrice == null || quotedPrice.compareTo(BigDecimal.ZERO) <= 0) {
            quotedPrice = item.getUnitPrice();
        }

        BigDecimal depositAmount = purchaseRequest.getDepositRequired();
        if (depositAmount == null || depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            depositAmount = quotedPrice.multiply(new BigDecimal("0.05"));
        }

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Phương thức thanh toán không hợp lệ");
        }

        Deposit deposit = Deposit.builder()
                .depositCode(generateDepositCode())
                .purchaseRequest(purchaseRequest)
                .user(user)
                .motorbike(item.getMotorbike())
                .quotedPrice(quotedPrice)
                .depositAmount(depositAmount)
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.UNPAID)
                .status(DepositStatus.PENDING)
                .note(request.getNote())
                .build();

        deposit = depositRepository.save(deposit);
        return mapToResponse(deposit);
    }

    @Override
    @Transactional
    public DepositResponse markPaid(Long id, User user) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt cọc"));

        if (!deposit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền thao tác");
        }

        if (deposit.getStatus() != DepositStatus.PENDING) {
            throw new RuntimeException("Đặt cọc này không còn ở trạng thái chờ");
        }

        deposit.setStatus(DepositStatus.PAID);
        deposit.setPaymentStatus(PaymentStatus.DEPOSIT_PAID);
        deposit.setPaidAt(LocalDateTime.now());

        depositRepository.save(deposit);
        return mapToResponse(deposit);
    }

    @Override
    public DepositResponse getById(Long id, User user) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt cọc"));

        if (!deposit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xem");
        }

        return mapToResponse(deposit);
    }

    @Override
    public List<DepositResponse> getMyDeposits(User user) {
        return depositRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<DepositResponse> getAllForAdmin() {
        return depositRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public DepositResponse adminConfirm(Long id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt cọc"));

        if (deposit.getStatus() != DepositStatus.PAID) {
            throw new RuntimeException("Chỉ xác nhận được khi khách đã thanh toán");
        }

        deposit.setStatus(DepositStatus.CONFIRMED);
        deposit.setConfirmedAt(LocalDateTime.now());

        PurchaseRequest purchaseRequest = deposit.getPurchaseRequest();
        purchaseRequest.setStatus(PurchaseRequestStatus.CONVERTED);

        depositRepository.save(deposit);
        purchaseRequestRepository.save(purchaseRequest);

        return mapToResponse(deposit);
    }

    private String generateDepositCode() {
        return "DEP" + System.currentTimeMillis();
    }

    private DepositResponse mapToResponse(Deposit deposit) {
        return new DepositResponse(
                deposit.getId(),
                deposit.getDepositCode(),
                deposit.getPurchaseRequest().getId(),
                deposit.getPurchaseRequest().getRequestCode(),
                deposit.getMotorbike().getId(),
                deposit.getMotorbike().getName(),
                deposit.getMotorbike().getSlug(),
                deposit.getMotorbike().getPrimaryImageUrl(),
                deposit.getQuotedPrice(),
                deposit.getDepositAmount(),
                deposit.getPaymentMethod().name(),
                deposit.getPaymentStatus().name(),
                deposit.getStatus().name(),
                deposit.getNote(),
                deposit.getPaidAt(),
                deposit.getConfirmedAt(),
                deposit.getCreatedAt(),

                "MB",                    // bankCode
                "1234567899",            // bankAccount
                "MB BANK",               // bankName
                "NGUYEN PHAM TAN AN"     // accountName
        );
    }
}