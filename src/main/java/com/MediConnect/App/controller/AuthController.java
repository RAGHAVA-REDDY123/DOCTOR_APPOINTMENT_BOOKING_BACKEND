package com.MediConnect.App.controller;

import com.MediConnect.App.dto.request.AuthRequest;
import com.MediConnect.App.dto.response.AuthResponse;
import com.MediConnect.App.dto.response.UserResponse;
import com.MediConnect.App.entity.User;
import com.MediConnect.App.enums.Role;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.mapper.EntityMapper;
import com.MediConnect.App.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody AuthRequest request) {
        String normalizedRole = request.getRole() == null ? "PATIENT" : request.getRole().trim().toUpperCase(Locale.ROOT);
        Role role;
        try {
            role = Role.valueOf(normalizedRole);
        } catch (IllegalArgumentException ex) {
            throw new ApiException("Invalid role. Allowed values: PATIENT, DOCTOR, ADMIN");
        }

        User user = authService.register(request.getName(), request.getEmail(), request.getPassword(), request.getPhone(), role);
        return ResponseEntity.ok(EntityMapper.toUserResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, null));
    }
}
