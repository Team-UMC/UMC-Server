package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.university.entity.University;

public interface UniversityService {
    University findUniversityByName(String universityName);
}
