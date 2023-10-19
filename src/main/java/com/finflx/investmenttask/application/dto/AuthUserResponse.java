package com.finflx.investmenttask.application.dto;

import com.finflx.investmenttask.domain.enumuration.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserResponse {
    private Long id;
    private String email;
    private UserRole scopeType;
    private String accessToken;
    private String refreshToken;
}
