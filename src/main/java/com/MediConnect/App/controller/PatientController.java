package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.PatientRequest;
import com.MediConnect.App.dto.response.PatientResponse;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/profile")
    public ResponseEntity<PatientResponse> createPatientProfile(@RequestBody PatientRequest request) {
        Patient p = patientService.createPatientProfile(request.getUserId(), request.getDateOfBirth(), request.getGender(), request.getBloodGroup(), request.getHeightCm(), request.getWeightKg(), request.getEmergencyContactName(), request.getEmergencyContactPhone());
        return ResponseEntity.ok(EntityMapper.toPatientResponse(p));
    }

    @PutMapping("/profile/{patientId}")
    public ResponseEntity<PatientResponse> updatePatientProfile(@PathVariable Long patientId, @RequestBody PatientRequest request) {
        Patient p = patientService.updatePatientProfile(patientId, request.getDateOfBirth(), request.getGender(), request.getBloodGroup(), request.getHeightCm(), request.getWeightKg(), request.getEmergencyContactName(), request.getEmergencyContactPhone());
        return ResponseEntity.ok(EntityMapper.toPatientResponse(p));
    }

    @DeleteMapping("/profile/{patientId}")
    public ResponseEntity<Void> deletePatientProfile(@PathVariable Long patientId) {
        patientService.deletePatientProfile(patientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<PatientResponse> getPatientProfile(@PathVariable Long userId) {
        Patient p = patientService.getPatientProfile(userId);
        return ResponseEntity.ok(EntityMapper.toPatientResponse(p));
    }
}
