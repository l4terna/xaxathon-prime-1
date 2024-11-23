package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.dto.AuthResponseDto;
import com.laterna.xaxaxa.dto.LoginRequestDto;
import com.laterna.xaxaxa.dto.RegisterRequestDto;
import com.laterna.xaxaxa.dto.TokenBlacklist;
import com.laterna.xaxaxa.entity.User;
import com.laterna.xaxaxa.exception.ResourceNotFoundException;
import com.laterna.xaxaxa.repository.TokenRepository;
import com.laterna.xaxaxa.repository.UserRepository;
import com.laterna.xaxaxa.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final SecurityUtil securityUtil;


    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)));
    }

    @Transactional
    public void logout(String token) {
        try {
            String cleanToken = token;
            if (token.startsWith("Bearer ")) {
                cleanToken = token.substring(7);
            }

            // Находим токен в базе
            var tokenEntity = tokenRepository.findByToken(cleanToken)
                    .orElseThrow(() -> new IllegalStateException("Token not found"));

            // Помечаем токен как отозванный и истекший
            tokenEntity.setRevoked(true);
            tokenEntity.setExpired(true);

            tokenRepository.save(tokenEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process logout", e);
        }
    }

    public List<Long> getAllUserIds() {
        return userRepository.findAllUserIds();
    }


    @Transactional(readOnly = true)
    public User getCurrentUser() {
        return securityUtil.getCurrentUser();
    }

    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String token = jwtService.generateToken(
                user
        );

        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}