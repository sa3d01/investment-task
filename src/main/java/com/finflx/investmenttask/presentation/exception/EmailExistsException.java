package com.finflx.investmenttask.presentation.exception;

import com.finflx.investmenttask.domain.enumuration.AuthErrorType;
import com.finflx.investmenttask.presentation.exception.base.BadRequestException;

/**
 * Custom exception to handle cases where an email already exists.
 */
public class EmailExistsException extends BadRequestException {
    /**
     * Constructor for EmailExistsException.
     *
     * @param email The email that already exists.
     */
    public EmailExistsException(String email) {
        super(AuthErrorType.EMAIL_EXISTS, "Email already exists.");
        addErrorContextItem("email", email);
    }
}
