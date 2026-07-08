package com.MediConnect.App.service;

import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.enums.AppointmentStatus;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.AppointmentRepository;
import com.MediConnect.App.repository.DoctorRepository;
import com.MediConnect.App.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleService scheduleService;

    @Transactional
    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime startAt) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        // Lock the doctor row to prevent concurrent booking and availability changes for the same doctor
        Doctor lockedDoctor = doctorRepository.findByIdForUpdate(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));

        if (!lockedDoctor.isAvailable()) {
            throw new ApiException("Doctor is not available");
        }

        var availability = scheduleService.validateSlotForBooking(lockedDoctor, startAt);
        LocalDateTime endAt = startAt.plusMinutes(availability.getSlotDurationMinutes());

        // Check overlapping appointments under pessimistic lock
        java.util.List<Appointment> overlapping = appointmentRepository.findOverlappingAppointmentsForUpdate(lockedDoctor, startAt, endAt);
        if (!overlapping.isEmpty()) {
            throw new ApiException("The selected slot is already booked");
        }

        Appointment appointment = Appointment.builder()
                .bookingId(UUID.randomUUID())
                .patient(patient)
                .doctor(lockedDoctor)
                .startAt(startAt)
                .endAt(endAt)
                .consultationFee(lockedDoctor.getConsultationFee())
                .build();

        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getPatientHistory(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        return appointmentRepository.findByPatientOrderByStartAtDesc(patient);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));
        return appointmentRepository.findByDoctorOrderByStartAtDesc(doctor);
    }

    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ApiException("Appointment is already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }
}
