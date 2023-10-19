package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.application.dto.*;
import com.finflx.investmenttask.domain.entity.RefreshToken;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.infrastructure.security.UserDetailsDto;
import com.finflx.investmenttask.infrastructure.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService,
                       RefreshTokenService refreshTokenService,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthUserResponse register(RegisterRequest request) {
        userService.validateEmailNotExists(request.getEmail());
        userService.create(request.getEmail(), request.getPassword(), request.getScopeType());
        return authenticateAndGenerateResponse(request.getEmail(), request.getPassword());
    }

    @Transactional
    public AuthUserResponse login(LoginRequest request) {
        return authenticateAndGenerateResponse(request.getEmail(), request.getPassword());
    }

    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshTokenEntity = refreshTokenService.getByToken(request.getRefreshToken());
        refreshTokenService.verifyExpiration(refreshTokenEntity);
        User user = refreshTokenEntity.getUser();
        String newToken = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getScopeType().toString());
        return new RefreshTokenResponse(newToken, request.getRefreshToken());
    }

    private AuthUserResponse authenticateAndGenerateResponse(String email, String password) {
        UserDetailsDto userDetailsDto = userService.loadUserByUsername(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateToken(
                userDetailsDto.getId(),
                userDetailsDto.getUsername(),
                userDetailsDto.getScopeType().toString()
        );
        RefreshToken refreshToken = refreshTokenService.generateForUser(userDetailsDto.toUser());
        AuthUserResponse response = new AuthUserResponse();
        response.setId(userDetailsDto.getId());
        response.setEmail(userDetailsDto.getUsername());
        response.setScopeType(userDetailsDto.getScopeType());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        return response;
    }
}
