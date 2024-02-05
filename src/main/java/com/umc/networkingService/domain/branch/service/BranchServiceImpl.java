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
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import com.umc.networkingService.global.common.exception.RestApiException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchUniversityRepository branchUniversityRepository;
    private final S3FileComponent s3FileComponent;

    private static final String BRANCH_CATEGORY = "branch";
    private static final Integer PAGE_SIZE = 10;

    @Transactional         //지부 생성
    public UUID postBranch(BranchRequest.PostBranchDTO request) {

        validateBranchNameAndDescription(request.getName(), request.getDescription());
        Branch newBranch = BranchConverter
                .toBranch
                        (request
                        ,uploadImageS3(BRANCH_CATEGORY,request.getImage())
                        );

        Branch savedBranch = branchRepository.save(newBranch);
        if (savedBranch.getId() == null) {
            throw new BranchHandler(ErrorCode.BRANCH_SAVE_FAIL);
        }
        return savedBranch.getId();
    }

    @Transactional          //지부 수정
    public UUID patchBranch(BranchRequest.PatchBranchDTO request) {

        Optional<Branch> optionalBranch = branchRepository.findById(request.getBranchId());
        if(optionalBranch.isEmpty()){
            throw new BranchHandler(ErrorCode.BRANCH_NOT_FOUND);
        }

        validateBranchNameAndDescription(request.getName(), request.getDescription());

        Branch branch = optionalBranch.get();
        branch.updateBranch(request, uploadImageS3(BRANCH_CATEGORY, request.getImage()));
        return branch.getId();

    }

    @Transactional          //지부 삭제
    public UUID deleteBranch(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId); //이미 검증된 branchId가 들어옴
        if(optionalBranch.isEmpty()){
            throw new BranchHandler(ErrorCode.BRANCH_NOT_FOUND);
        }
        Branch branch = optionalBranch.get();
        branchUniversityRepository.deleteByBranch(branch);
        branchRepository.delete(branch);
        return branchId;
    }

    @Transactional(readOnly = true)        //지부 리스트 조회
    public BranchResponse.JoinBranchs joinBranchList(Semester semester) {
        if(semester == null){
            throw new BranchHandler(ErrorCode.SEMESTER_NOT_VALID);
        }

        List<Branch> branchs = branchRepository.findAllBySemester(semester);
        return BranchConverter.toJoinBranchListDTO(branchs);
    }

    @Transactional(readOnly = true)        //지부 상세 조회
    public BranchResponse.JoinBranchDetails joinBranchDetail(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if(optionalBranch.isEmpty()){
            throw new BranchHandler(ErrorCode.BRANCH_NOT_FOUND);
        }
        Branch branch = optionalBranch.get();

        List<BranchUniversity> branchUniversities
                = branchUniversityRepository.findAllByBranch(branch);

        List<University> universityList = branchUniversities.stream()
                .map(BranchUniversity::getUniversity)
                .toList();


        return BranchConverter.toJoinBranchDetailDTO(universityList);

    }

    //s3에 이미지 업로드
    private String uploadImageS3(String category,MultipartFile image){
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
            throw new BranchHandler(ErrorCode.BRANCH_NAME_EMPTY);
        }
        if(description.isEmpty()){
            throw new BranchHandler(ErrorCode.BRANCH_DESCRIPTION_EMPTY);
        }
    }

    @Override
    public Branch loadEntity(UUID id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_BRANCH));
    }
}
