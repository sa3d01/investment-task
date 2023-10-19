package com.finflx.investmenttask.presentation.exception.base;

import java.util.HashMap;
import java.util.Map;

public class BaseException extends RuntimeException {
    private final ErrorType errorType;
    private final Map<String, Object> errorContext;

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.errorContext = new HashMap<>();
    }

    public BaseException(ErrorType errorType, String message, Map<String, Object> errorContext) {
        super(message);
        this.errorType = errorType;
        this.errorContext = errorContext;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Map<String, Object> getErrorContext() {
        return errorContext;
    }

    public void addErrorContextItem(String key, Object value) {
        this.errorContext.put(key, value);
    }
}
