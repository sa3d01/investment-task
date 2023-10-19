package com.finflx.investmenttask.presentation.exception;

import com.finflx.investmenttask.domain.enumuration.CommonErrorType;
import com.finflx.investmenttask.presentation.exception.base.ForbiddenException;

public class NotEligibleToUpdateEntityException extends ForbiddenException {
    public NotEligibleToUpdateEntityException(String entity, Long entityId) {
        super(CommonErrorType.NOT_ELIGIBLE_TO_UPDATE_ENTITY, "Not eligible to update this entity.");
        addErrorContextItem("entity", entity);
        addErrorContextItem("entityId", entityId);
    }

    public NotEligibleToUpdateEntityException(String message) {
        super(CommonErrorType.NOT_ELIGIBLE_TO_UPDATE_ENTITY, message);
    }
}
