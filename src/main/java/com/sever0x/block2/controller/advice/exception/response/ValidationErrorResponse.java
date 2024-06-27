package com.sever0x.block2.controller.advice.exception.response;

import java.util.List;

public record ValidationErrorResponse(List<FieldValidationError> errors) {
}
