package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.exception.code.BranchErrorCode;
import com.umc.networkingService.global.common.exception.code.GlobalErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BranchUniversityServiceImpl implements BranchUniversityService {

    private final BranchUniversityRepository branchUniversityRepository;

    @Override
    public Branch findBranchByUniversity(University university) {

        return branchUniversityRepository.findByUniversityAndIsActive(university, Boolean.TRUE)
                .orElseThrow(() -> new RestApiException(BranchErrorCode.EMPTY_BRANCH))
                .getBranch();

    }
}
