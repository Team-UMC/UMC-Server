package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.converter.BranchConverter;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchUniversityRepository branchUniversityRepository;
    private final S3FileComponent s3FileComponent;

    private static final String BRANCH_CATEGORY = "branch";
    private static final Integer PAGE_SIZE = 10;

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

    @Transactional          //지부 삭제
    public void deleteBranch(UUID branchId) {
        Branch branch = branchRepository.findById(branchId);
        branchRepository.delete(branch);
    }

    @Transactional(readOnly = true)        //지부 리스트 조회
    public BranchResponse.JoinBranchListDTO joinBranchList(Integer page) {

        Page<Branch> branchPage = branchRepository.findAll(PageRequest.of(page, PAGE_SIZE));


        return BranchConverter.toJoinBranchListDTO(branchPage.getContent());
    }

    @Transactional(readOnly = true)        //todo:지부 상세 조회
    public BranchResponse.JoinBranchDetailDTO joinBranchDetail(UUID branchId) {
        List<BranchUniversity> branchUniversities
                = branchUniversityRepository.findByBranch(
                        branchRepository.findById(branchId)
        );


        return BranchConverter.toJoinBranchDetailDTO(branch);
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
