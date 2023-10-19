package com.finflx.investmenttask.presentation.exception.base;

public class BadRequestException extends BaseException {
    public BadRequestException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
