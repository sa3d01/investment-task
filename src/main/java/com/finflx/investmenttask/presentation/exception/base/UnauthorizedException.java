package com.finflx.investmenttask.presentation.exception.base;

import com.finflx.investmenttask.domain.enumuration.AuthErrorType;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(AuthErrorType.UNAUTHORIZED, "Unauthorized access.");
    }
}
