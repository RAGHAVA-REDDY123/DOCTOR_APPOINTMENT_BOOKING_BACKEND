package com.MediConnect.App.service;

import com.MediConnect.App.entity.RefreshToken;
import com.MediConnect.App.entity.User;
import com.MediConnect.App.enums.Role;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.RefreshTokenRepository;
import com.MediConnect.App.repository.UserRepository;
import com.MediConnect.App.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public User register(String name, String email, String password, String phone, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email already exists");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new ApiException("Phone already exists");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .phone(phone)
                .role(role)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public String login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));

        String jwt = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build();
        refreshTokenRepository.save(refreshToken);
        return jwt;
    }
}
