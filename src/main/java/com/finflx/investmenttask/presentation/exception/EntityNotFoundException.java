package com.finflx.investmenttask.presentation.exception;

import com.finflx.investmenttask.domain.enumuration.CommonErrorType;
import com.finflx.investmenttask.presentation.exception.base.BadRequestException;

public class EntityNotFoundException extends BadRequestException {
    public EntityNotFoundException(String entity, String key, Object value) {
        super(CommonErrorType.ENTITY_NOT_FOUND, entity + " not found.");
        addErrorContextItem("entity", entity);
        addErrorContextItem(key, value);
    }
}
