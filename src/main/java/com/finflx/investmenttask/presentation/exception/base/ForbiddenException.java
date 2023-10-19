package com.finflx.investmenttask.presentation.exception.base;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
