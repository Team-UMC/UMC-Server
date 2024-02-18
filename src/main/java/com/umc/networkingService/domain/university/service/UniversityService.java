package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.EntityLoader;

import java.util.UUID;

public interface UniversityService extends EntityLoader<University,UUID> {
    University findUniversityByName(String universityName);

}