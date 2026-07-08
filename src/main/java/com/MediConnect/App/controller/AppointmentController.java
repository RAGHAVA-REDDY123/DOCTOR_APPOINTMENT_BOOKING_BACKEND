package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.AppointmentRequest;
import com.MediConnect.App.dto.response.AppointmentResponse;
import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody AppointmentRequest request) {
        Appointment ap = appointmentService.bookAppointment(request.getPatientId(), request.getDoctorId(), request.getStartAt());
        AppointmentResponse res = new AppointmentResponse();
        res.setBookingId(ap.getBookingId());
        res.setPatientId(ap.getPatient().getId());
        res.setDoctorId(ap.getDoctor().getId());
        res.setStartAt(ap.getStartAt());
        res.setEndAt(ap.getEndAt());
        res.setStatus(ap.getStatus());
        res.setConsultationFee(ap.getConsultationFee());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getPatientHistory(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getPatientHistory(patientId);
        return ResponseEntity.ok(appointments.stream().map(EntityMapper::toAppointmentResponse).collect(Collectors.toList()));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments.stream().map(EntityMapper::toAppointmentResponse).collect(Collectors.toList()));
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(EntityMapper.toAppointmentResponse(appointment));
    }
}
