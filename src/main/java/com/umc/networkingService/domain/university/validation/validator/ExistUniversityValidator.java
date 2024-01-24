package com.umc.networkingService.domain.university.validation.validator;

import com.umc.networkingService.domain.university.service.UniversityServiceImpl;
import com.umc.networkingService.domain.university.validation.annotation.ExistUniversity;
import com.umc.networkingService.global.common.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExistUniversityValidator implements ConstraintValidator<ExistUniversity, UUID> {

    private final UniversityServiceImpl universityService;

    @Override
    public void initialize(ExistUniversity constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(UUID values, ConstraintValidatorContext context) {
        boolean isValid = universityService.isUniversityValid(values);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.EMPTY_UNIVERSITY.getMessage()).addConstraintViolation();
        }

        return isValid;

    }

}

