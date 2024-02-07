package com.umc.networkingService.domain.branch.validation.validater;

import com.umc.networkingService.domain.branch.service.BranchServiceImpl;
import com.umc.networkingService.domain.branch.validation.annotation.ExistBranch;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.code.BranchErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExistBranchValidator implements ConstraintValidator<ExistBranch, UUID> {

    private final BranchServiceImpl branchService;

    @Override
    public void initialize(ExistBranch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(UUID values, ConstraintValidatorContext context) {
        boolean isValid = branchService.isBranchValid(values);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(BranchErrorCode.BRANCH_NOT_FOUND.getMessage()).addConstraintViolation();
        }

        return isValid;

    }

}
