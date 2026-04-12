package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.DepositStatus;
import com.example.DoAnJ2EE.common.constant.PaymentMethod;
import com.example.DoAnJ2EE.common.constant.PaymentStatus;
import com.example.DoAnJ2EE.dto.request.CreateDepositRequest;
import com.example.DoAnJ2EE.dto.request.RefundDepositRequest;
import com.example.DoAnJ2EE.dto.response.DepositResponse;
import com.example.DoAnJ2EE.dto.response.PayOSCreateResponse;
import com.example.DoAnJ2EE.entity.Deposit;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.DepositRepository;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.service.DepositService;
import com.example.DoAnJ2EE.service.MailService;
import com.example.DoAnJ2EE.service.PayOSService;
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
    private final MotorbikeRepository motorbikeRepository;
    private final PayOSService payOSService;
    private final MailService mailService;

    @Override
    @Transactional
    public DepositResponse create(User user, CreateDepositRequest request) {
        if (request.getMotorbikeId() == null) {
            throw new RuntimeException("Thiếu motorbikeId");
        }

        Motorbike motorbike = motorbikeRepository.findById(request.getMotorbikeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        if (!Boolean.TRUE.equals(motorbike.getIsActive())) {
            throw new RuntimeException("Xe hiện không khả dụng");
        }

        BigDecimal price = motorbike.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá xe không hợp lệ");
        }

        BigDecimal depositAmount = price.multiply(new BigDecimal("0.05"));

        Deposit deposit = Deposit.builder()
                .depositCode("DEP" + System.currentTimeMillis())
                .user(user)
                .motorbike(motorbike)
                .quotedPrice(price)
                .depositAmount(depositAmount)
                .paymentMethod(PaymentMethod.PAYOS)
                .paymentStatus(PaymentStatus.UNPAID)
                .status(DepositStatus.PENDING)
                .note(request.getNote())
                .build();

        deposit = depositRepository.save(deposit);

        PayOSCreateResponse payos = payOSService.createDepositPayment(deposit);

        deposit.setPayosOrderCode(payos.getOrderCode());
        deposit.setCheckoutUrl(payos.getCheckoutUrl());
        deposit.setQrCode(payos.getQrCode());

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
        deposit.setPaymentStatus(PaymentStatus.PAID);
        deposit.setPaidAt(LocalDateTime.now());

        deposit = depositRepository.save(deposit);
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

        deposit = depositRepository.save(deposit);
        return mapToResponse(deposit);
    }

    @Override
    public DepositResponse getAdminById(Long id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt cọc"));

        return mapToResponse(deposit);
    }

    @Override
    @Transactional
    public DepositResponse updateStatusByAdmin(Long id, String status, String note) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt cọc"));

        DepositStatus newStatus;
        try {
            newStatus = DepositStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái đặt cọc không hợp lệ");
        }

        deposit.setStatus(newStatus);

        if (note != null && !note.isBlank()) {
            deposit.setNote(note);
        }

        if (newStatus == DepositStatus.PAID) {
            deposit.setPaymentStatus(PaymentStatus.DEPOSIT_PAID);
            if (deposit.getPaidAt() == null) {
                deposit.setPaidAt(LocalDateTime.now());
            }
        }

        if (newStatus == DepositStatus.CONFIRMED) {
            deposit.setPaymentStatus(PaymentStatus.DEPOSIT_PAID);
            if (deposit.getConfirmedAt() == null) {
                deposit.setConfirmedAt(LocalDateTime.now());
            }
        }

        if (newStatus == DepositStatus.CANCELLED) {
            deposit.setPaymentStatus(PaymentStatus.UNPAID);
        }

        if (newStatus == DepositStatus.REFUNDED) {
            deposit.setPaymentStatus(PaymentStatus.REFUNDED);
        }

        deposit = depositRepository.save(deposit);

        if (deposit.getUser() != null
                && deposit.getUser().getEmail() != null
                && !deposit.getUser().getEmail().isBlank()) {

            String html = """
                <h3>Đơn đặt cọc của bạn đã được cập nhật</h3>
                <p><b>Mã đặt cọc:</b> %s</p>
                <p><b>Xe:</b> %s</p>
                <p><b>Trạng thái mới:</b> %s</p>
                <p><b>Ghi chú:</b> %s</p>
                """
                    .formatted(
                            deposit.getDepositCode(),
                            deposit.getMotorbike().getName(),
                            deposit.getStatus().name(),
                            deposit.getNote() != null ? deposit.getNote() : "Không có"
                    );

            try {
                mailService.sendMail(
                        deposit.getUser().getEmail(),
                        "Cập nhật đơn đặt cọc",
                        html
                );
            } catch (Exception e) {
                System.out.println("Gửi mail deposit lỗi: " + e.getMessage());
            }
        }

        return mapToResponse(deposit);
    }

    @Override
    @Transactional
    public void refundPaidDeposit(Long depositId, RefundDepositRequest request, User user) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt cọc"));

        if (deposit.getUser() == null || !deposit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền hoàn tiền đặt cọc này");
        }

        if (deposit.getStatus() != DepositStatus.PAID && deposit.getStatus() != DepositStatus.CONFIRMED) {
            throw new RuntimeException("Chỉ hoàn tiền cho đặt cọc đã thanh toán");
        }

        if (deposit.getPaymentStatus() != PaymentStatus.PAID
                && deposit.getPaymentStatus() != PaymentStatus.DEPOSIT_PAID) {
            throw new RuntimeException("Đặt cọc này chưa ở trạng thái đã thanh toán");
        }

        if (request.getToBin() == null || request.getToBin().isBlank()) {
            throw new RuntimeException("Thiếu mã ngân hàng nhận hoàn tiền");
        }

        if (request.getToAccountNumber() == null || request.getToAccountNumber().isBlank()) {
            throw new RuntimeException("Thiếu số tài khoản nhận hoàn tiền");
        }

        String referenceId = "refund_" + deposit.getId() + "_" + System.currentTimeMillis();
        String reason = (request.getReason() == null || request.getReason().isBlank())
                ? "Khong co"
                : request.getReason();

        System.out.println("=== REFUND START ===");
        System.out.println("depositId = " + deposit.getId());
        System.out.println("depositCode = " + deposit.getDepositCode());
        System.out.println("refund toBin = " + request.getToBin());
        System.out.println("refund toAccountNumber = " + request.getToAccountNumber());
        System.out.println("refund amount = " + deposit.getDepositAmount());

//        payOSService.createRefundPayout(
//                referenceId,
//                deposit.getDepositAmount().longValue(),
//                "Hoan tien " + deposit.getDepositCode(),
//                request.getToBin(),
//                request.getToAccountNumber()
//        );

        deposit.setStatus(DepositStatus.REFUNDED);
        deposit.setPaymentStatus(PaymentStatus.REFUNDED);

        String oldNote = deposit.getNote() == null ? "" : deposit.getNote() + "\n";
        String refundNote = oldNote
                + "Yeu cau hoan tien thanh cong. "
                + "Ngan hang(BIN): " + request.getToBin()
                + ", STK: " + request.getToAccountNumber()
                + ", Ly do: " + reason;

        deposit.setNote(refundNote);

        deposit = depositRepository.save(deposit);

        System.out.println("=== REFUND DONE ===");
        System.out.println("refund referenceId = " + referenceId);
        System.out.println("deposit status = " + deposit.getStatus());
        System.out.println("payment status = " + deposit.getPaymentStatus());

        if (deposit.getUser() != null
                && deposit.getUser().getEmail() != null
                && !deposit.getUser().getEmail().isBlank()) {

            String html = """
                <h3>Yêu cầu hoàn tiền đặt cọc đã được xử lý</h3>
                <p><b>Mã đặt cọc:</b> %s</p>
                <p><b>Xe:</b> %s</p>
                <p><b>Số tiền hoàn:</b> %s</p>
                <p><b>Ngân hàng nhận:</b> %s</p>
                <p><b>Số tài khoản nhận:</b> %s</p>
                <p><b>Lý do:</b> %s</p>
                <p><b>Trạng thái:</b> %s</p>
                """
                    .formatted(
                            deposit.getDepositCode(),
                            deposit.getMotorbike() != null ? deposit.getMotorbike().getName() : "Không xác định",
                            deposit.getDepositAmount(),
                            request.getToBin(),
                            request.getToAccountNumber(),
                            reason,
                            deposit.getStatus().name()
                    );

            try {
                mailService.sendMail(
                        deposit.getUser().getEmail(),
                        "Hoàn tiền đặt cọc",
                        html
                );
            } catch (Exception e) {
                System.out.println("Gửi mail refund lỗi: " + e.getMessage());
            }
        }
    }

    private DepositResponse mapToResponse(Deposit deposit) {
        Long purchaseRequestId = null;
        String requestCode = null;

        if (deposit.getPurchaseRequest() != null) {
            purchaseRequestId = deposit.getPurchaseRequest().getId();
            requestCode = deposit.getPurchaseRequest().getRequestCode();
        }

        Long motorbikeId = null;
        String motorbikeName = null;
        String motorbikeSlug = null;
        String motorbikeImage = null;

        if (deposit.getMotorbike() != null) {
            motorbikeId = deposit.getMotorbike().getId();
            motorbikeName = deposit.getMotorbike().getName();
            motorbikeSlug = deposit.getMotorbike().getSlug();
            motorbikeImage = deposit.getMotorbike().getPrimaryImageUrl();
        }

        String paymentMethod = deposit.getPaymentMethod() != null ? deposit.getPaymentMethod().name() : null;
        String paymentStatus = deposit.getPaymentStatus() != null ? deposit.getPaymentStatus().name() : null;
        String status = deposit.getStatus() != null ? deposit.getStatus().name() : null;

        return new DepositResponse(
                deposit.getId(),
                deposit.getDepositCode(),
                purchaseRequestId,
                requestCode,
                motorbikeId,
                motorbikeName,
                motorbikeSlug,
                motorbikeImage,
                deposit.getQuotedPrice(),
                deposit.getDepositAmount(),
                paymentMethod,
                paymentStatus,
                status,
                deposit.getNote(),
                deposit.getPaidAt(),
                deposit.getConfirmedAt(),
                deposit.getCreatedAt(),
                "MB",
                "1234567899",
                "MB BANK",
                "NGUYEN PHAM TAN AN",
                deposit.getPayosOrderCode(),
                deposit.getCheckoutUrl(),
                deposit.getQrCode(),
                deposit.getTransactionCode()
        );
    }
}