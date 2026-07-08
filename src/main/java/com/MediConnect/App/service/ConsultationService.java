package com.MediConnect.App.service;

import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.entity.Consultation;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.AppointmentRepository;
import com.MediConnect.App.repository.ConsultationRepository;
import com.MediConnect.App.repository.DoctorRepository;
import com.MediConnect.App.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Consultation createConsultation(Long appointmentId, Long doctorId, Long patientId,
                                           String chiefComplaint, String diagnosis, String consultationNotes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException("Appointment not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));

        Consultation consultation = Consultation.builder()
                .appointment(appointment)
                .doctor(doctor)
                .patient(patient)
                .chiefComplaint(chiefComplaint)
                .diagnosis(diagnosis)
                .consultationNotes(consultationNotes)
                .build();

        return consultationRepository.save(consultation);
    }
}
