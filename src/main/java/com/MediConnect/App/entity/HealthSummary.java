package com.MediConnect.App.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_summaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthSummary extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @Column(length = 1000)
    private String medicalConditions;

    @Column(length = 1000)
    private String currentMedications;

    @Column(length = 1000)
    private String allergies;

    @Column(length = 1000)
    private String previousSurgeries;

    @Column(length = 1000)
    private String familyMedicalHistory;

    @Column(length = 1000)
    private String additionalNotes;
}
