package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BranchErrorCode;
import com.umc.networkingService.global.common.exception.code.UniversityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchUniversityServiceImpl implements BranchUniversityService {

    private final BranchRepository branchRepository;
    private final UniversityRepository universityRepository;
    private final BranchUniversityRepository branchUniversityRepository;

    @Override
    public Branch findBranchByUniversityAndSemester(University university, Semester lastSemester) {

        return branchUniversityRepository.findByUniversityAndBranch_Semester(university, lastSemester)
                .orElseThrow(() -> new RestApiException(BranchErrorCode.BRANCH_NOT_FOUND))
                .getBranch();

    }

    //지부, 대학 연결하기
    @Transactional
    public BranchResponse.ConnectBranch connectBranchUniversity(UUID branchId, List<UUID> universityIds) {
        //유효한 지부가 값으로 들어옴
        universityIds.forEach(universityId -> {
            if(!universityRepository.existsById(universityId)){ //유효한 대학인지 확인
                throw new RestApiException(UniversityErrorCode.EMPTY_UNIVERSITY);
            }
            if (branchUniversityRepository.existsByBranchIdAndUniversityId(branchId, universityId)) { //지부-대학 연결 여부 확인
                throw new RestApiException(BranchErrorCode.BRANCH_UNIVERSITY_ALREADY_EXIST); //이미 연결됨
            }
            branchUniversityRepository.save
                    (
                    BranchUniversity.builder()
                            .isActive(Boolean.TRUE)
                            .branch(branchRepository.findById(branchId).get())
                            .university(universityRepository.findById(universityId).get())
                            .build()
            );
        });
        return BranchResponse.ConnectBranch.builder()
                .branchId(branchId)
                .universityIds(universityIds).build();
    }

    //지부, 대학 연결 끊기
    @Transactional
    public BranchResponse.ConnectBranch disconnectBranchUniversity(UUID branchId, List<UUID> universityIds) {
        //지부가 값으로 들어옴
        universityIds.forEach(universityId -> {
            if(!universityRepository.existsById(universityId)){ //유효한 대학인지 확인
                throw new RestApiException(UniversityErrorCode.EMPTY_UNIVERSITY);
            }
            if (!branchUniversityRepository.existsByBranchIdAndUniversityId(branchId, universityId)) { //지부-대학 연결 여부 확인
                throw new RestApiException(BranchErrorCode.BRANCH_UNIVERSITY_NOT_FOUND); //연결되지 않음
            }
            branchUniversityRepository.deleteByBranchIdAndUniversityId(branchId, universityId);
        });
        return BranchResponse.ConnectBranch.builder()
                .branchId(branchId)
                .universityIds(universityIds).build();
    }
}
