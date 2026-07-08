package com.MediConnect.App.controller;

import com.MediConnect.App.dto.response.AppointmentResponse;
import com.MediConnect.App.dto.response.DoctorResponse;
import com.MediConnect.App.dto.response.PatientResponse;
import com.MediConnect.App.dto.response.UserResponse;
import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.entity.User;
import com.MediConnect.App.enums.Role;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(EntityMapper::toUserResponse).collect(Collectors.toList()));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<Doctor> doctors = adminService.getAllDoctors();
        return ResponseEntity.ok(doctors.stream().map(EntityMapper::toDoctorResponse).collect(Collectors.toList()));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<Patient> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients.stream().map(EntityMapper::toPatientResponse).collect(Collectors.toList()));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<Appointment> appts = adminService.getAllAppointments();
        return ResponseEntity.ok(appts.stream().map(EntityMapper::toAppointmentResponse).collect(Collectors.toList()));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long userId, @RequestParam Role role) {
        User u = adminService.updateUserRole(userId, role);
        return ResponseEntity.ok(EntityMapper.toUserResponse(u));
    }
}
