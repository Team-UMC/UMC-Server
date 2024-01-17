package com.umc.networkingService.validation.annotation;

import com.umc.networkingService.validation.validater.ValidPageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented //사용자 정의 어노테이션을 만들 때
@Constraint(validatedBy = ValidPageValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPage {
    String message() default "페이지 범위가 0 이하 입니다.";
    Class<?>[] groups() default {}; //그룹핑
    Class<? extends Payload>[] payload() default {}; //메타데이터
}