package com.umc.networkingService.domain.university.service;

<<<<<<< HEAD
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
=======
import com.umc.networkingService.domain.university.entity.University;

public interface UniversityService {
    University findUniversityByName(String universityName);
>>>>>>> 9a5c384ff89a20278f29f42c6165fd78f74392cf
}
