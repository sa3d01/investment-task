package com.finflx.investmenttask.application.dto;

import com.finflx.investmenttask.presentation.exception.base.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ErrorResponse {
    private final int status;
    private final String errorType;
    private final Map<String, Object> errorContext;
    private final List<String> messages;
    private final LocalDateTime time;
    private final String stackTrace;

    public ErrorResponse(HttpStatus status,
                         ErrorType errorType,
                         Map<String, Object> errorContext,
                         List<String> messages,
                         String stackTrace) {
        this.status = status.value();
        this.errorType = errorType == null ? "" : errorType.toString();
        this.errorContext = errorContext;
        this.messages = messages;
        this.time = LocalDateTime.now();
        this.stackTrace = stackTrace;
    }
}
