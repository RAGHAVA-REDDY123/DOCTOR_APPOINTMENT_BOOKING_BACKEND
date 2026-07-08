package com.MediConnect.App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_availability", indexes = {
        @Index(name = "idx_doctor_availability_doctor", columnList = "doctor_id"),
        @Index(name = "idx_doctor_availability_doctor_day_time", columnList = "doctor_id,day_of_week,start_time")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_doctor_availability_slot", columnNames = {"doctor_id", "day_of_week", "start_time"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailability extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "slot_duration_minutes", nullable = false)
    @Builder.Default
    private int slotDurationMinutes = 30;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
