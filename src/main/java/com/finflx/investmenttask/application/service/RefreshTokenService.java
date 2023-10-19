package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.domain.entity.RefreshToken;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Integer tokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               @Value("${jwt.expiration}") Integer tokenExpiration) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenExpiration = tokenExpiration;
    }


    public RefreshToken create(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(new User(userId));
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(tokenExpiration / 1000));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken generateForUser(User user) {
        refreshTokenRepository.deleteByUserAndExpiryDateBefore(user, LocalDateTime.now());
        List<RefreshToken> refreshTokens = refreshTokenRepository.findByUser(user);
        if (refreshTokens.isEmpty()) {
            return create(user.getId());
        } else {
            return refreshTokens.get(0);
        }
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow();
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new IllegalArgumentException("ExpiredRefreshTokenException");
        }
    }
}
