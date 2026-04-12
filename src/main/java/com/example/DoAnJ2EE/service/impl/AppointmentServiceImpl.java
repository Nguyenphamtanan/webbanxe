package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.request.CreateAppointmentRequest;
import com.example.DoAnJ2EE.dto.response.AppointmentResponse;
import com.example.DoAnJ2EE.entity.Appointment;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.AppointmentRepository;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.DoAnJ2EE.service.MailService;
import com.example.DoAnJ2EE.dto.response.AdminAppointmentResponse;
import com.example.DoAnJ2EE.dto.request.UpdateAppointmentStatusRequest;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MotorbikeRepository motorbikeRepository;
    private final MailService mailService;

    @Override
    public AppointmentResponse create(User user, CreateAppointmentRequest request) {

        Motorbike bike = motorbikeRepository.findById(request.getMotorbikeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        Appointment appointment = Appointment.builder()
                .appointmentCode("APT-" + UUID.randomUUID().toString().substring(0, 8))
                .user(user)
                .motorbike(bike)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .appointmentTime(request.getAppointmentTime())
                .note(request.getNote())
                .status("PENDING")
                .build();

        appointmentRepository.save(appointment);

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentCode(),
                appointment.getMotorbike() != null ? appointment.getMotorbike().getName() : null,
                appointment.getMotorbike() != null ? appointment.getMotorbike().getPrimaryImageUrl() : null,
                appointment.getMotorbike() != null ? appointment.getMotorbike().getSlug() : null,
                appointment.getAppointmentTime(),
                appointment.getPhone(),
                appointment.getNote(),
                appointment.getStatus()
        );
    }

    @Override
    public List<AppointmentResponse> getMy(User user) {
        return appointmentRepository.findByUserId(user.getId())
                .stream()
                .map(a -> new AppointmentResponse(
                        a.getId(),
                        a.getAppointmentCode(),
                        a.getMotorbike() != null ? a.getMotorbike().getName() : null,
                        a.getMotorbike() != null ? a.getMotorbike().getPrimaryImageUrl() : null,
                        a.getMotorbike() != null ? a.getMotorbike().getSlug() : null,
                        a.getAppointmentTime(),
                        a.getPhone(),
                        a.getNote(),
                        a.getStatus()
                ))
                .toList();
    }
    @Override
    public List<AdminAppointmentResponse> getAllForAdmin() {
        return appointmentRepository.findAll()
                .stream()
                .map(a -> new AdminAppointmentResponse(
                        a.getId(),
                        a.getAppointmentCode(),
                        a.getFullName(),
                        a.getPhone(),
                        a.getMotorbike().getName(),
                        a.getAppointmentTime(),
                        a.getNote(),
                        a.getStatus(),
                        a.getCreatedAt(),
                        a.getResponseNote()
                ))
                .toList();
    }

    @Override
    public AdminAppointmentResponse getByIdForAdmin(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        return new AdminAppointmentResponse(
                a.getId(),
                a.getAppointmentCode(),
                a.getFullName(),
                a.getPhone(),
                a.getMotorbike().getName(),
                a.getAppointmentTime(),
                a.getNote(),
                a.getStatus(),
                a.getCreatedAt(),
                a.getResponseNote()
        );
    }

    @Override
    public AdminAppointmentResponse updateStatus(Long id, UpdateAppointmentStatusRequest request) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        // update status
        a.setStatus(request.getStatus().toUpperCase());

        // 🔥 thêm dòng này (quan trọng nhất)
        a.setResponseNote(request.getResponseNote());

        appointmentRepository.save(a);

        // 🔥 gửi mail có nội dung phản hồi
        if (a.getUser() != null && a.getUser().getEmail() != null && !a.getUser().getEmail().isBlank()) {

            String html = """
            <h3>Lịch hẹn của bạn đã được cập nhật</h3>
            <p><b>Mã lịch:</b> %s</p>
            <p><b>Xe:</b> %s</p>
            <p><b>Thời gian:</b> %s</p>
            <p><b>Trạng thái:</b> %s</p>
            <p><b>Nội dung phản hồi:</b> %s</p>
            """
                    .formatted(
                            a.getAppointmentCode(),
                            a.getMotorbike().getName(),
                            a.getAppointmentTime(),
                            a.getStatus(),
                            a.getResponseNote() != null ? a.getResponseNote() : "Không có"
                    );

            mailService.sendMail(
                    a.getUser().getEmail(),
                    "Cập nhật lịch hẹn",
                    html
            );
        }

        return new AdminAppointmentResponse(
                a.getId(),
                a.getAppointmentCode(),
                a.getFullName(),
                a.getPhone(),
                a.getMotorbike().getName(),
                a.getAppointmentTime(),
                a.getNote(),
                a.getStatus(),
                a.getCreatedAt(),
                a.getResponseNote() // 🔥 thêm
        );

    }
}