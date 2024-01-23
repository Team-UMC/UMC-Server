package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.university.converter.UniversityConverter;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    @Override
    public University findUniversityByName(String universityName) {
        return universityRepository.findByName(universityName)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_UNIVERSITY));
    }

    public UniversityResponse.joinUniversityList joinUniversityList(){
        List<University> universityList = universityRepository.findAll();
        return UniversityConverter.toJoinUniversityList(universityList);
    }
}
