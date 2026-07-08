package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.HealthSummaryRequest;
import com.MediConnect.App.dto.response.HealthSummaryResponse;
import com.MediConnect.App.entity.HealthSummary;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.HealthSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-summary")
@RequiredArgsConstructor
public class HealthSummaryController {

    private final HealthSummaryService healthSummaryService;

    @PutMapping("/{patientId}")
    public ResponseEntity<HealthSummaryResponse> updateHealthSummary(@PathVariable Long patientId, @RequestBody HealthSummaryRequest request) {
        HealthSummary updated = healthSummaryService.updateHealthSummary(patientId, request.getMedicalConditions(), request.getCurrentMedications(), request.getAllergies(), request.getPreviousSurgeries(), request.getFamilyMedicalHistory(), request.getAdditionalNotes());
        return ResponseEntity.ok(EntityMapper.toHealthSummaryResponse(updated));
    }
}
