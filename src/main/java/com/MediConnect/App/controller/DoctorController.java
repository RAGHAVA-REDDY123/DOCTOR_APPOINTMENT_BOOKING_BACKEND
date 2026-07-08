package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.DoctorAvailabilityRequest;
import com.MediConnect.App.dto.request.DoctorBlockRequest;
import com.MediConnect.App.dto.request.DoctorRequest;
import com.MediConnect.App.dto.request.BulkAvailabilityRequest;
import com.MediConnect.App.dto.response.AvailableDateResponse;
import com.MediConnect.App.dto.response.DateBasedSlotResponse;
import com.MediConnect.App.dto.response.DoctorAvailabilityResponse;
import com.MediConnect.App.dto.response.DoctorBlockResponse;
import com.MediConnect.App.dto.response.DoctorResponse;
import com.MediConnect.App.dto.response.PatientResponse;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.DoctorAvailability;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.DoctorService;
import com.MediConnect.App.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final ScheduleService scheduleService;

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(@RequestParam(required = false) String specialization,
                                                              @RequestParam(required = false) String city,
                                                              @RequestParam(required = false) String hospital) {
        List<Doctor> doctors = doctorService.searchDoctors(specialization, city, hospital);
        return ResponseEntity.ok(doctors.stream().map(EntityMapper::toDoctorResponse).collect(Collectors.toList()));
    }

    @PostMapping("/profile")
    public ResponseEntity<DoctorResponse> createDoctorProfile(@RequestBody DoctorRequest request) {
        Doctor d = doctorService.createDoctorProfile(request.getUserId(), request.getRegistrationNumber(), request.getSpecialization(), request.getQualification(), request.getExperienceYears(), request.getConsultationFee(), request.getHospitalName(), request.getClinicAddress(), request.getCity(), request.getState(), request.getBio());
        return ResponseEntity.ok(EntityMapper.toDoctorResponse(d));
    }

    @PutMapping("/profile/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctorProfile(@PathVariable Long doctorId, @RequestBody DoctorRequest request) {
        Doctor d = doctorService.updateDoctorProfile(doctorId, request.getRegistrationNumber(), request.getSpecialization(), request.getQualification(), request.getExperienceYears(), request.getConsultationFee(), request.getHospitalName(), request.getClinicAddress(), request.getCity(), request.getState(), request.getBio());
        return ResponseEntity.ok(EntityMapper.toDoctorResponse(d));
    }

    @DeleteMapping("/profile/{doctorId}")
    public ResponseEntity<Void> deleteDoctorProfile(@PathVariable Long doctorId) {
        doctorService.deleteDoctorProfile(doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/availabilities")
    public ResponseEntity<List<DoctorAvailabilityResponse>> getDoctorAvailabilities(@PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getDoctorAvailabilities(doctorId).stream()
                .map(EntityMapper::toDoctorAvailabilityResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{doctorId}/availabilities")
    public ResponseEntity<DoctorAvailabilityResponse> createDoctorAvailability(@PathVariable Long doctorId,
                                                                               @RequestBody DoctorAvailabilityRequest request) {
        return ResponseEntity.ok(EntityMapper.toDoctorAvailabilityResponse(scheduleService.createDoctorAvailability(doctorId, request)));
    }

    @PostMapping("/{doctorId}/availabilities/bulk")
    public ResponseEntity<List<DoctorAvailabilityResponse>> createBulkAvailability(@PathVariable Long doctorId,
                                                                                   @RequestBody BulkAvailabilityRequest request) {
        List<DoctorAvailability> availabilities = scheduleService.createBulkAvailability(doctorId, request);
        return ResponseEntity.ok(availabilities.stream()
                .map(EntityMapper::toDoctorAvailabilityResponse)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{doctorId}/availabilities/{availabilityId}")
    public ResponseEntity<DoctorAvailabilityResponse> updateDoctorAvailability(@PathVariable Long doctorId,
                                                                               @PathVariable Long availabilityId,
                                                                               @RequestBody DoctorAvailabilityRequest request) {
        return ResponseEntity.ok(EntityMapper.toDoctorAvailabilityResponse(scheduleService.updateDoctorAvailability(doctorId, availabilityId, request)));
    }

    @DeleteMapping("/{doctorId}/availabilities/{availabilityId}")
    public ResponseEntity<Void> deleteDoctorAvailability(@PathVariable Long doctorId,
                                                         @PathVariable Long availabilityId) {
        scheduleService.deleteDoctorAvailability(doctorId, availabilityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/blocks")
    public ResponseEntity<List<DoctorBlockResponse>> getDoctorBlocks(@PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getDoctorBlocks(doctorId).stream()
                .map(EntityMapper::toDoctorBlockResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{doctorId}/blocks")
    public ResponseEntity<DoctorBlockResponse> createDoctorBlock(@PathVariable Long doctorId,
                                                                 @RequestBody DoctorBlockRequest request) {
        return ResponseEntity.ok(EntityMapper.toDoctorBlockResponse(scheduleService.createDoctorBlock(doctorId, request)));
    }

    @DeleteMapping("/{doctorId}/blocks/{blockId}")
    public ResponseEntity<Void> deleteDoctorBlock(@PathVariable Long doctorId,
                                                  @PathVariable Long blockId) {
        scheduleService.deleteDoctorBlock(doctorId, blockId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/available-slots")
    public ResponseEntity<List<DateBasedSlotResponse>> getAvailableSlots(@PathVariable Long doctorId,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getAvailableSlotsByDate(doctorId, date));
    }

    @GetMapping("/{doctorId}/calendar")
    public ResponseEntity<List<AvailableDateResponse>> getCalendarAvailability(@PathVariable Long doctorId,
                                                                               @RequestParam String month) {
        return ResponseEntity.ok(scheduleService.getCalendarAvailability(doctorId, month));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<PatientResponse> getPatientProfile(@PathVariable Long patientId) {
        Patient p = doctorService.getPatientProfile(patientId);
        return ResponseEntity.ok(EntityMapper.toPatientResponse(p));
    }
}
