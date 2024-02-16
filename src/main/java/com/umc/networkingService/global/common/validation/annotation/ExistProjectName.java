package com.umc.networkingService.global.common.validation.annotation;

import com.umc.networkingService.global.common.validation.validator.ProjectNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ProjectNameValidator.class)
public @interface ExistProjectName {

    String message() default "이미 존재하는 프로젝트 명입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
