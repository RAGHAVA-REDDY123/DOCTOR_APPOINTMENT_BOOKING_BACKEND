package com.MediConnect.App.entity;

import com.MediConnect.App.enums.DoctorBlockType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_blocks", indexes = {
        @Index(name = "idx_doctor_blocks_doctor", columnList = "doctor_id"),
        @Index(name = "idx_doctor_blocks_period", columnList = "start_at,end_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorBlock extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorBlockType type;

    @Column(nullable = false, length = 500)
    private String reason;
}
