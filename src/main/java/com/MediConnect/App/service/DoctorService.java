package com.MediConnect.App.service;

import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.entity.User;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.DoctorRepository;
import com.MediConnect.App.repository.PatientRepository;
import com.MediConnect.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "doctors", key = "T(java.util.Objects).hash(#specialization, #city, #hospital)")
    public List<Doctor> searchDoctors(String specialization, String city, String hospital) {
        if (specialization != null && !specialization.isBlank()) {
            return doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
        }
        if (city != null && !city.isBlank()) {
            return doctorRepository.findByCityContainingIgnoreCase(city);
        }
        if (hospital != null && !hospital.isBlank()) {
            return doctorRepository.findByHospitalNameContainingIgnoreCase(hospital);
        }
        return doctorRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "doctors", allEntries = true)
    public Doctor createDoctorProfile(Long userId, String registrationNumber, String specialization,
                                      String qualification, Integer experienceYears, BigDecimal consultationFee,
                                      String hospitalName, String clinicAddress, String city, String state, String bio) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
        Doctor doctor = Doctor.builder()
                .user(user)
                .registrationNumber(registrationNumber)
                .specialization(specialization)
                .qualification(qualification)
                .experienceYears(experienceYears)
                .consultationFee(consultationFee)
                .hospitalName(hospitalName)
                .clinicAddress(clinicAddress)
                .city(city)
                .state(state)
                .bio(bio)
                .build();
        return doctorRepository.save(doctor);
    }

    @Transactional
    @CacheEvict(value = "doctors", allEntries = true)
    public Doctor updateDoctorProfile(Long doctorId, String registrationNumber, String specialization,
                                      String qualification, Integer experienceYears, BigDecimal consultationFee,
                                      String hospitalName, String clinicAddress, String city, String state, String bio) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));
        if (registrationNumber != null) doctor.setRegistrationNumber(registrationNumber);
        if (specialization != null) doctor.setSpecialization(specialization);
        if (qualification != null) doctor.setQualification(qualification);
        if (experienceYears != null) doctor.setExperienceYears(experienceYears);
        if (consultationFee != null) doctor.setConsultationFee(consultationFee);
        if (hospitalName != null) doctor.setHospitalName(hospitalName);
        if (clinicAddress != null) doctor.setClinicAddress(clinicAddress);
        if (city != null) doctor.setCity(city);
        if (state != null) doctor.setState(state);
        if (bio != null) doctor.setBio(bio);
        return doctorRepository.save(doctor);
    }

    @Transactional
    @CacheEvict(value = "doctors", allEntries = true)
    public void deleteDoctorProfile(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));
        doctorRepository.delete(doctor);
    }

    @Transactional(readOnly = true)
    public Patient getPatientProfile(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
    }
}
