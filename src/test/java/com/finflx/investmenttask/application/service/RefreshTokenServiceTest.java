package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.application.service.RefreshTokenService;
import com.finflx.investmenttask.domain.entity.RefreshToken;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRefreshToken() {
        Long userId = 1L;
        User user = new User(userId);

        int tokenExpiration = 3600;
        refreshTokenService = new RefreshTokenService(refreshTokenRepository, tokenExpiration);

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));


        RefreshToken refreshToken = refreshTokenService.create(userId);

        assertNotNull(refreshToken);
        assertEquals(user.getId(), refreshToken.getUser().getId());
        assertNotNull(refreshToken.getExpiryDate());
        assertNotNull(refreshToken.getToken());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testGenerateForUserWithNoExistingTokens() {
        Long userId = 1L;
        User user = new User(userId);

        when(refreshTokenRepository.findByUser(user)).thenReturn(new ArrayList<>());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));


        int tokenExpiration = 3600;
        refreshTokenService = new RefreshTokenService(refreshTokenRepository, tokenExpiration);


        RefreshToken refreshToken = refreshTokenService.generateForUser(user);

        assertNotNull(refreshToken);
        assertEquals(user.getId(), refreshToken.getUser().getId());
        verify(refreshTokenRepository, times(1)).findByUser(user);
        verify(refreshTokenRepository, times(1)).deleteByUserAndExpiryDateBefore(any(), any());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testGenerateForUserWithExistingTokens() {
        Long userId = 1L;
        User user = new User(userId);
        RefreshToken existingToken = new RefreshToken();

        List<RefreshToken> existingTokens = new ArrayList<>();
        existingTokens.add(existingToken);

        when(refreshTokenRepository.findByUser(user)).thenReturn(existingTokens);

        RefreshToken refreshToken = refreshTokenService.generateForUser(user);

        assertNotNull(refreshToken);
        assertEquals(existingToken, refreshToken);
        verify(refreshTokenRepository, times(1)).findByUser(user);
        verify(refreshTokenRepository, times(0)).deleteByUserAndExpiryDateBefore(user, LocalDateTime.now());
        verify(refreshTokenRepository, times(0)).save(any(RefreshToken.class));
    }

    @Test
    void testGetByToken() {
        String tokenValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();

        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(refreshToken));

        RefreshToken retrievedToken = refreshTokenService.getByToken(tokenValue);

        assertNotNull(retrievedToken);
        assertEquals(refreshToken, retrievedToken);
    }

    @Test
    void testGetByTokenNotFound() {
        String tokenValue = UUID.randomUUID().toString();

        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> refreshTokenService.getByToken(tokenValue));
    }

    @Test
    void testVerifyExpirationNotExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));

        assertDoesNotThrow(() -> refreshTokenService.verifyExpiration(refreshToken));
    }

    @Test
    void testVerifyExpirationExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDateTime.now().minusMinutes(10));

        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.verifyExpiration(refreshToken));
        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }
}
