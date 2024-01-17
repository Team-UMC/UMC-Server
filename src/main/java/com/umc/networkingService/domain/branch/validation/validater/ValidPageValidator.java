package com.umc.networkingService.domain.branch.validation.validater;

import com.umc.networkingService.domain.branch.validation.annotation.ValidPage;
import com.umc.networkingService.global.common.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidPageValidator implements ConstraintValidator<ValidPage, Integer> {

    @Override
    public void initialize(ValidPage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer page, ConstraintValidatorContext context) {
        boolean isValid = page > 0;
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.PAGE_NOT_VALID.getMessage()).addConstraintViolation();
        }
        return isValid;
    }

}
