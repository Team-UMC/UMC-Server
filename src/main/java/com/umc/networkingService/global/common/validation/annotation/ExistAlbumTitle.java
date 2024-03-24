package com.umc.networkingService.global.common.validation.annotation;

import com.umc.networkingService.global.common.validation.validator.AlbumTitleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AlbumTitleValidator.class)
public @interface ExistAlbumTitle {

    String message() default "이미 존재하는 사진첩 제목입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}