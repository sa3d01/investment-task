package com.finflx.investmenttask.application.dto;

import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Email is required.")
    @Size(max = 100, message = "Email cannot exceed 100 characters.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(max = 100, message = "Password cannot exceed 50 characters.")
    private String password;

    @NotNull
    private UserRole scopeType;
}
