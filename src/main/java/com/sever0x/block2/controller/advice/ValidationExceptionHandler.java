package com.sever0x.block2.controller.advice;

import com.sever0x.block2.controller.advice.exception.response.FieldValidationError;
import com.sever0x.block2.controller.advice.exception.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Stream;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldValidationError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldValidationError(error.getField(), error.getDefaultMessage()))
                .toList();

        List<FieldValidationError> objectErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> new FieldValidationError(error.getObjectName(), error.getDefaultMessage()))
                .toList();

        List<FieldValidationError> errors = Stream.concat(fieldErrors.stream(), objectErrors.stream())
                .toList();

        return new ValidationErrorResponse(errors);
    }
}