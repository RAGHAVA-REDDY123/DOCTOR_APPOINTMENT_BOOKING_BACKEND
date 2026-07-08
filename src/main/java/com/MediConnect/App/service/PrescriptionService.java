package com.MediConnect.App.service;

import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.entity.Prescription;
import com.MediConnect.App.entity.PrescriptionItem;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.AppointmentRepository;
import com.MediConnect.App.repository.DoctorRepository;
import com.MediConnect.App.repository.PatientRepository;
import com.MediConnect.App.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Prescription createPrescription(Long appointmentId, Long doctorId, Long patientId, String notes, List<PrescriptionItemRequest> items) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ApiException("Appointment not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));

        Prescription prescription = Prescription.builder()
                .prescriptionNumber(UUID.randomUUID())
                .appointment(appointment)
                .doctor(doctor)
                .patient(patient)
                .issuedAt(LocalDateTime.now())
                .notes(notes)
                .build();

        Prescription saved = prescriptionRepository.save(prescription);
        if (items != null) {
            for (PrescriptionItemRequest request : items) {
                PrescriptionItem item = PrescriptionItem.builder()
                        .prescription(saved)
                        .medicineName(request.medicineName())
                        .dosage(request.dosage())
                        .frequency(request.frequency())
                        .durationDays(request.durationDays())
                        .instructions(request.instructions())
                        .build();
                saved.getItems().add(item);
            }
        }
        return prescriptionRepository.save(saved);
    }

    public record PrescriptionItemRequest(String medicineName, String dosage, String frequency, Integer durationDays, String instructions) {
    }
}
