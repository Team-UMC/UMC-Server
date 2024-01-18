package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.university.entity.University;

public interface BranchUniversityService {
    Branch findBranchByUniversity(University university);
}
