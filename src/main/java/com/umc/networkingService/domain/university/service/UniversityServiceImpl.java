package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    @Override
    public University findUniversityByName(String universityName) {
        return universityRepository.findByName(universityName)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_UNIVERSITY));
    }
}
