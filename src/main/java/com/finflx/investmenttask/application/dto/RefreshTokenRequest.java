package com.finflx.investmenttask.application.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is required.")
    @Size(max = 100, message = "Refresh token cannot exceed 100 characters.")
    private String refreshToken;
}
