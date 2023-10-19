package com.finflx.investmenttask.presentation.exception;

import com.finflx.investmenttask.domain.enumuration.CommonErrorType;
import com.finflx.investmenttask.application.dto.ErrorResponse;
import com.finflx.investmenttask.presentation.exception.base.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) {

        HttpStatus statusCode;
        List<String> errors;
        ErrorType errorType = null;
        Map<String, Object> errorContext = new ConcurrentHashMap<>();
        String stackTrace = getStackTraceAsString(ex);

        if (ex instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException dive = (DataIntegrityViolationException) ex;
            statusCode = HttpStatus.BAD_REQUEST;
            errors = List.of(ex.getMessage());
            if (dive.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) dive.getCause();
                errors = List.of(cve.getConstraintName()+" already exists.");
                errorType = CommonErrorType.CONSTRAINT_VIOLATION;
            }
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) ex;
            statusCode = HttpStatus.BAD_REQUEST;
            errors = validationException.getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

        } else if (ex instanceof BaseException) {
            BaseException exception = (BaseException) ex;
            statusCode = getStatusFromException(exception);
            errors = List.of(exception.getMessage());
            errorType = exception.getErrorType();
            errorContext = exception.getErrorContext();

        } else if (ex instanceof AccessDeniedException) {
            statusCode = HttpStatus.FORBIDDEN;
            errors = List.of("Access to this API is forbidden due to insufficient role.");
            errorType = CommonErrorType.INSUFFICIENT_ROLE;

        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
            errors = List.of(ex.getMessage());
        }

        return ResponseEntity.status(statusCode)
                .body(new ErrorResponse(statusCode, errorType, errorContext, errors, stackTrace));
    }

    private HttpStatus getStatusFromException(BaseException ex) {
        if (ex instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof UnauthorizedException) {
            return HttpStatus.UNAUTHORIZED;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
