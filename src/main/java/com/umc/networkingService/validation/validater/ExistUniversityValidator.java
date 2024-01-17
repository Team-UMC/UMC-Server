package com.umc.networkingService.validation.validater;

import com.umc.networkingService.domain.branch.service.BranchService;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.validation.annotation.ExistUniversity;
import com.umc.networkingService.global.common.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExistUniversityValidator implements ConstraintValidator<ExistUniversity, UUID> {

    private final UniversityService universityService;

    @Override
    public void initialize(ExistUniversity constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(UUID values, ConstraintValidatorContext context) {
        boolean isValid = universityService.isUniversityValid(values);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.UNIVERSITY_NOT_FOUND.getMessage()).addConstraintViolation();
        }

        return isValid;

    }

}