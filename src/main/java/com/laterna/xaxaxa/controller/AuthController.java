package com.laterna.xaxaxa.controller;

import com.laterna.xaxaxa.dto.AuthResponseDto;
import com.laterna.xaxaxa.dto.LoginRequestDto;
import com.laterna.xaxaxa.dto.RegisterRequestDto;
import com.laterna.xaxaxa.entity.User;
import com.laterna.xaxaxa.repository.UserRepository;
import com.laterna.xaxaxa.service.JwtService;
import com.laterna.xaxaxa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
