package com.umc.networkingService.global.common.validation.validator;

import com.umc.networkingService.domain.project.repository.ProjectRepository;
import com.umc.networkingService.global.common.validation.annotation.ExistProjectName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectNameValidator implements ConstraintValidator<ExistProjectName, String> {

    private final ProjectRepository projectRepository;

    @Override
    public void initialize(ExistProjectName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return  !projectRepository.existsByName(value);
    }
}
