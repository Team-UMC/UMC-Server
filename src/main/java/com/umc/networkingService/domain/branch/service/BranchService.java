package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.converter.BranchConverter;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final S3FileComponent s3FileComponent;

    private static final String BRANCH_CATEGORY = "branch";

    @Transactional
    public void postBranch(BranchRequest.PostBranchDTO request) {

        //semaster 검증
        if(request.getSemester() == null){
            throw new RestApiException(ErrorCode.EMPTY_BRANCH_SEMESTER);
        }

        Branch newBranch = BranchConverter
                .toBranch
                        (request
                        ,s3FileComponent.uploadFile(BRANCH_CATEGORY,request.getImage())
                        );

        branchRepository.save(newBranch);
    }

}
