package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BranchErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService{

    private final BranchRepository branchRepository;

    @Override
    public Branch loadEntity(UUID id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RestApiException(BranchErrorCode.EMPTY_BRANCH));
    }
}
