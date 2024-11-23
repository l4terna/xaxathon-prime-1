package com.laterna.xaxaxa.util;

import com.laterna.xaxaxa.entity.Token;
import com.laterna.xaxaxa.entity.User;
import com.laterna.xaxaxa.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final TokenRepository tokenRepository;

    public User getCurrentUser() {
        String token = extractTokenFromRequest();
        Token tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (tokenEntity.isRevoked() || tokenEntity.isExpired() || tokenEntity.getExpiresAt().before(new Date())) {
            throw new RuntimeException("Token is not valid");
        }

        return tokenEntity.getUser();
    }

    private String extractTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        throw new RuntimeException("No Bearer token found in request");
    }
}
