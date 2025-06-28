package org.ncapas.happypawsbackend.services;

import jakarta.transaction.Transactional;
import org.ncapas.happypawsbackend.Domain.Entities.RefreshToken;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiredAt(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiredAt().isBefore(Instant.now());
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            System.out.println("✅ Token revocado: " + token); // debug opcional
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    public Optional<RefreshToken> getByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void revokeTokenByUser(User user) {
        refreshTokenRepository.deleteByUser(user); // este borra, no revoca
    }
}

