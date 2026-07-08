package com.MediConnect.App.service;

import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.entity.User;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.PatientRepository;
import com.MediConnect.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Transactional
    public Patient createPatientProfile(Long userId, LocalDate dateOfBirth, String gender, String bloodGroup,
                                        Double heightCm, Double weightKg, String emergencyContactName,
                                        String emergencyContactPhone) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(dateOfBirth)
                .gender(gender == null ? null : com.MediConnect.App.enums.Gender.valueOf(gender))
                .bloodGroup(bloodGroup == null ? null : com.MediConnect.App.enums.BloodGroup.valueOf(bloodGroup))
                .heightCm(heightCm)
                .weightKg(weightKg)
                .emergencyContactName(emergencyContactName)
                .emergencyContactPhone(emergencyContactPhone)
                .build();
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatientProfile(Long patientId, LocalDate dateOfBirth, String gender, String bloodGroup,
                                        Double heightCm, Double weightKg, String emergencyContactName,
                                        String emergencyContactPhone) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        if (dateOfBirth != null) patient.setDateOfBirth(dateOfBirth);
        if (gender != null) patient.setGender(com.MediConnect.App.enums.Gender.valueOf(gender));
        if (bloodGroup != null) patient.setBloodGroup(com.MediConnect.App.enums.BloodGroup.valueOf(bloodGroup));
        if (heightCm != null) patient.setHeightCm(heightCm);
        if (weightKg != null) patient.setWeightKg(weightKg);
        if (emergencyContactName != null) patient.setEmergencyContactName(emergencyContactName);
        if (emergencyContactPhone != null) patient.setEmergencyContactPhone(emergencyContactPhone);
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatientProfile(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        patientRepository.delete(patient);
    }

    @Transactional(readOnly = true)
    public Patient getPatientProfile(Long userId) {
        return patientRepository.findByUserEmail(userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found")).getEmail())
                .orElseThrow(() -> new ApiException("Patient profile not found"));
    }
}
