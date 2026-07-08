package com.MediConnect.App.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors", indexes = {
        @Index(name = "idx_doctors_specialization", columnList = "specialization"),
        @Index(name = "idx_doctors_city", columnList = "city"),
        @Index(name = "idx_doctors_available", columnList = "available")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String specialization;

    private String qualification;

    @Min(0)
    private Integer experienceYears;

    @Column(nullable = false)
    private BigDecimal consultationFee;

    private String hospitalName;

    private String clinicAddress;

    private String city;

    private String state;

    @Column(length = 1000)
    private String bio;

    private String languages;

    @Builder.Default
    private Double rating = 0.0;

    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    private boolean available = true;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayOfWeek ASC, startTime ASC")
    @Builder.Default
    private List<DoctorAvailability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("startAt ASC")
    @Builder.Default
    private List<DoctorBlock> blockedSlots = new ArrayList<>();
}
