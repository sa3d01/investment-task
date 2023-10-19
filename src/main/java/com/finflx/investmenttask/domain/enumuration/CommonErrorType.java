package com.finflx.investmenttask.domain.enumuration;

import com.finflx.investmenttask.presentation.exception.base.ErrorType;

public enum CommonErrorType implements ErrorType {
    ENTITY_NOT_FOUND,
    INSUFFICIENT_ROLE,
    NOT_ELIGIBLE_TO_UPDATE_ENTITY,
    CONSTRAINT_PAYMENT,
    CONSTRAINT_VIOLATION
}
