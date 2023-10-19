package com.finflx.investmenttask.domain.enumuration;

import com.finflx.investmenttask.presentation.exception.base.ErrorType;

public enum AuthErrorType implements ErrorType {
    UNAUTHORIZED,
    EMAIL_EXISTS,
}
