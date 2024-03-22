package com.umc.networkingService.global.common.validation.validator;

import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.project.repository.ProjectRepository;
import com.umc.networkingService.global.common.validation.annotation.ExistAlbumTitle;
import com.umc.networkingService.global.common.validation.annotation.ExistProjectName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumTitleValidator implements ConstraintValidator<ExistAlbumTitle, String> {

    private final AlbumRepository albumRepository;
    @Override
    public void initialize(ExistAlbumTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !albumRepository.existsByTitle(value);
    }
}
