package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.PrescriptionRequest;
import com.MediConnect.App.dto.response.PrescriptionResponse;
import com.MediConnect.App.entity.Prescription;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/create")
    public ResponseEntity<PrescriptionResponse> createPrescription(@RequestBody PrescriptionRequest request) {
        List<PrescriptionService.PrescriptionItemRequest> items = request.getItems() == null ? List.of() : request.getItems().stream()
                .map(item -> new PrescriptionService.PrescriptionItemRequest(
                        item.medicineName,
                        item.dosage,
                        item.frequency,
                        item.durationDays,
                        item.instructions))
                .collect(Collectors.toList());

        Prescription p = prescriptionService.createPrescription(
                request.getAppointmentId(),
                request.getDoctorId(),
                request.getPatientId(),
                request.getNotes(),
                items);

        return ResponseEntity.ok(EntityMapper.toPrescriptionResponse(p));
    }
}
