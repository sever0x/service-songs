package com.sever0x.block2.validation.annotation;

import com.sever0x.block2.validation.validator.AtLeastOneNotNullValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotNullValidator.class)
@NotNull(groups = AtLeastOneNotNull.class)
@Documented
public @interface AtLeastOneNotNull {
    String message() default "At least one field must be non-null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}