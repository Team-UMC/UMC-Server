package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.mapper.BranchMapper;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.utils.S3FileComponent;
import com.umc.networkingService.global.common.exception.code.BranchErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchUniversityRepository branchUniversityRepository;
    private final S3FileComponent s3FileComponent;

    private static final String BRANCH_CATEGORY = "branch";

    @Transactional         //지부 생성
    public BranchResponse.BranchId postBranch(BranchRequest.BranchInfoDTO request) {

        validateBranchNameAndDescription(request.getName(), request.getDescription());

        Branch savedBranch = branchRepository.save(
                BranchMapper.toBranch(request,uploadImageS3(BRANCH_CATEGORY,request.getImage()))
        );

        if (savedBranch.getId() == null) {
            throw new RestApiException(BranchErrorCode.BRANCH_SAVE_FAIL);
        }
        return BranchResponse.BranchId.builder()
                .branchId(savedBranch.getId()).build();
    }

    @Transactional          //지부 수정
    public BranchResponse.BranchId patchBranch(BranchRequest.BranchInfoDTO request, UUID branchId) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if(optionalBranch.isEmpty()){
            throw new RestApiException(BranchErrorCode.BRANCH_NOT_FOUND);
        }

        validateBranchNameAndDescription(request.getName(), request.getDescription());

        Branch branch = optionalBranch.get();
        branch.updateBranch(request, uploadImageS3(BRANCH_CATEGORY, request.getImage()));
        return BranchResponse.BranchId.builder()
                        .branchId(branch.getId()).build();

    }

    @Transactional          //지부 삭제
    public BranchResponse.BranchId deleteBranch(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if(optionalBranch.isEmpty()){
            throw new RestApiException(BranchErrorCode.BRANCH_NOT_FOUND);
        }
        Branch branch = optionalBranch.get();
        branchUniversityRepository.deleteByBranch(branch);
        branchRepository.delete(branch);
        return BranchResponse.BranchId.builder().branchId(branchId).build();
    }

    @Transactional(readOnly = true)        //지부 리스트 조회
    public BranchResponse.JoinBranchs joinBranchList(Semester semester) {
        if(semester == null){
            throw new RestApiException(BranchErrorCode.SEMESTER_NOT_VALID);
        }

        List<Branch> branchs = branchRepository.findAllBySemester(semester);
        return BranchMapper.toJoinBranchListDTO(branchs);
    }

    @Transactional(readOnly = true)        //지부 상세 조회
    public BranchResponse.JoinBranchDetails joinBranchDetail(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if(optionalBranch.isEmpty()){
            throw new RestApiException(BranchErrorCode.BRANCH_NOT_FOUND);
        }
        Branch branch = optionalBranch.get();

        List<BranchUniversity> branchUniversities
                = branchUniversityRepository.findAllByBranch(branch);

        List<University> universityList = branchUniversities.stream()
                .map(BranchUniversity::getUniversity)
                .toList();


        return BranchMapper.toJoinBranchDetailDTO(universityList);

    }

    //s3에 이미지 업로드
    private String uploadImageS3(String category, MultipartFile image){
        if(image == null || image.isEmpty()){
            return null;
        }
        return s3FileComponent.uploadFile(category, image);
    }

    //유효한 branchId인지 검증
    public boolean isBranchValid(UUID branchId){
        return branchRepository.existsById(branchId);
    }

    //이름, 설명 확인
    public void validateBranchNameAndDescription(String name, String description){
        if(name.isEmpty()){
            throw new RestApiException(BranchErrorCode.BRANCH_NAME_EMPTY);
        }
        if(description.isEmpty()){
            throw new RestApiException(BranchErrorCode.BRANCH_DESCRIPTION_EMPTY);
        }
    }

    @Override
    public Branch loadEntity(UUID id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RestApiException(BranchErrorCode.BRANCH_NOT_FOUND));
    }
}
