package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.entity.Token;
import com.laterna.xaxaxa.entity.User;
import com.laterna.xaxaxa.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenRepository tokenRepository;
    private static final String USER_ID_CLAIM = "userId";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    @Transactional
    public String generateToken(Map<String, Object> extraClaims, User user) {
        extraClaims.put(USER_ID_CLAIM, user.getId());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String jwtToken = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        var token = Token.builder()
                .token(jwtToken)
                .user(user)
                .tokenType("Bearer")
                .expired(false)
                .revoked(false)
                .createdAt(now)
                .expiresAt(expiryDate)
                .build();

        tokenRepository.save(token);

        return jwtToken;
    }

    @Transactional
    public boolean isTokenValid(String token, User user) {
        var tokenEntity = tokenRepository.findByToken(token)
                .orElse(null);

        if (tokenEntity == null || tokenEntity.isRevoked() || tokenEntity.isExpired()) {
            return false;
        }

        final Long userId = extractUserId(token);
        return userId != null &&
                userId.equals(user.getId()) &&
                !isTokenExpired(token);
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUserId(userId);
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return ((Number) claims.get(USER_ID_CLAIM)).longValue();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}