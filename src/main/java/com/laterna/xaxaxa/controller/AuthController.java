package com.laterna.xaxaxa.controller;

import com.laterna.xaxaxa.dto.AuthResponseDto;
import com.laterna.xaxaxa.dto.LoginRequestDto;
import com.laterna.xaxaxa.dto.RegisterRequestDto;
import com.laterna.xaxaxa.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Parameter(description = "Registration details", required = true)
            @Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String token
    ) {
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

}
