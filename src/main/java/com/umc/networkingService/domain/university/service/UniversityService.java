package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;


    //대학 존재 여부 확인
    public boolean isUniversityValid(UUID universityId) {
        return universityRepository.existsById(universityId);
    }
}
