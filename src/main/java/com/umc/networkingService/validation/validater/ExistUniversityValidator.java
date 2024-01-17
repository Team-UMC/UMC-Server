package com.umc.networkingService.validation.validater;

import com.umc.networkingService.domain.branch.service.BranchService;
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

    private final BranchService branchService;

    @Override
    public void initialize(ExistUniversity constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(UUID values, ConstraintValidatorContext context) {
        boolean isValid = branchService.isUniversityValid(values); //나중에 university로 바꾸면 더 좋을 듯?

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.BRANCH_NOT_FOUND.getMessage()).addConstraintViolation();
        }

        return isValid;

    }

}