package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.converter.BranchConverter;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.execption.BranchHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final S3FileComponent s3FileComponent;

    private static final String BRANCH_CATEGORY = "branch";

    @Transactional         //지부 생성
    public void postBranch(BranchRequest.PostBranchDTO request) {

        Branch newBranch = BranchConverter
                .toBranch
                        (request
                        ,uploadImageS3(BRANCH_CATEGORY,request.getImage())
                        );

        branchRepository.save(newBranch);
    }

    @Transactional          //지부 수정
    public void patchBranch(BranchRequest.PatchBranchDTO request) {

        Branch branch = branchRepository.findById(request.getBranchId()); //이미 검증된 branchId가 들어옴
        branch.updateBranch(request, uploadImageS3(BRANCH_CATEGORY,request.getImage()));
        branchRepository.save(branch); //save는 update와 insert를 모두 수행함
    }

    //s3에 이미지 업로드
    private String uploadImageS3(String category,MultipartFile image){
        if(image == null){
            return null;
        }
        return s3FileComponent.uploadFile(category, image);
    }

    //유효한 branchId인지 검증
    public boolean isBranchValid(UUID branchId){
        return branchRepository.existsById(branchId);
    }

}
