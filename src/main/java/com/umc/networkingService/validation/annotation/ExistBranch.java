package com.umc.networkingService.validation.annotation;

import com.umc.networkingService.validation.validater.ExistBranchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented //사용자 정의 어노테이션을 만들 때
@Constraint(validatedBy = ExistBranchValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME) //어노테이션 정보를 언제까지 유지할 것인가 (생명 주기)
public @interface ExistBranch {
    String message() default "아이디에 해당하는 지부가 존재하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
