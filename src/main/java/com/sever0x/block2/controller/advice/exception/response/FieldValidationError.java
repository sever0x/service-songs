package com.sever0x.block2.controller.advice.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldValidationError {

    private String object;

    private String message;
}
