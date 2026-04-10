package com.example.DoAnJ2EE.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_code", nullable = false, unique = true)
    private String appointmentCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "motorbike_id", nullable = false)
    private Motorbike motorbike;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status; // PENDING, CONFIRMED, CANCELLED

    @Column(name = "response_note")
    private String responseNote;

    @CreationTimestamp
    private LocalDateTime createdAt;
}