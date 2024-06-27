package com.sever0x.block2.validation.validator;

import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.validation.annotation.AtLeastOneNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, GenerateReportSongsRequest> {
    @Override
    public boolean isValid(GenerateReportSongsRequest value, ConstraintValidatorContext context) {
        return value.artistId() != null || value.album() != null;
    }
}
