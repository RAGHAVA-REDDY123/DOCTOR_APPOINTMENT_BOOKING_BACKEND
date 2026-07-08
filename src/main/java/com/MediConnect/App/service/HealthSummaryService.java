package com.MediConnect.App.service;

import com.MediConnect.App.entity.HealthSummary;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.HealthSummaryRepository;
import com.MediConnect.App.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthSummaryService {

    private final HealthSummaryRepository healthSummaryRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public HealthSummary updateHealthSummary(Long patientId, String medicalConditions, String currentMedications,
                                             String allergies, String previousSurgeries,
                                             String familyMedicalHistory, String additionalNotes) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        HealthSummary summary = healthSummaryRepository.findByPatient(patient).orElseGet(() -> {
            HealthSummary created = HealthSummary.builder().patient(patient).build();
            return created;
        });

        summary.setMedicalConditions(medicalConditions);
        summary.setCurrentMedications(currentMedications);
        summary.setAllergies(allergies);
        summary.setPreviousSurgeries(previousSurgeries);
        summary.setFamilyMedicalHistory(familyMedicalHistory);
        summary.setAdditionalNotes(additionalNotes);

        return healthSummaryRepository.save(summary);
    }
}
