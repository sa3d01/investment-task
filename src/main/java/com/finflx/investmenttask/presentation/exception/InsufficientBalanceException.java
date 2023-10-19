package com.finflx.investmenttask.presentation.exception;

import com.finflx.investmenttask.domain.enumuration.CommonErrorType;
import com.finflx.investmenttask.presentation.exception.base.BadRequestException;

public class InsufficientBalanceException extends BadRequestException {
    public InsufficientBalanceException(String message) {
        super(CommonErrorType.CONSTRAINT_PAYMENT, message);
    }
}
