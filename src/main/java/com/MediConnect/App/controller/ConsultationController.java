package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.ConsultationRequest;
import com.MediConnect.App.dto.response.ConsultationResponse;
import com.MediConnect.App.entity.Consultation;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/create")
    public ResponseEntity<ConsultationResponse> createConsultation(@RequestBody ConsultationRequest request) {
        Consultation c = consultationService.createConsultation(request.getAppointmentId(), request.getDoctorId(), request.getPatientId(), request.getChiefComplaint(), request.getDiagnosis(), request.getConsultationNotes());
        return ResponseEntity.ok(EntityMapper.toConsultationResponse(c));
    }
}
