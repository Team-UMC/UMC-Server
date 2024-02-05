package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchUniversityHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchUniversityServiceImpl implements BranchUniversityService {

    //todo : 관리자 권한 확인 추가하기

    private final BranchRepository branchRepository;
    private final UniversityRepository universityRepository;
    private final BranchUniversityRepository branchUniversityRepository;

    @Override
    public Branch findBranchByUniversityAndSemester(University university, Semester lastSemester) {

        return branchUniversityRepository.findByUniversityAndBranch_Semester(university, lastSemester)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_BRANCH))
                .getBranch();

    }

    //지부, 대학 연결하기  todo : 현재 기수 여부(isActive) 처리 로직 없음
    @Transactional
    public void connectBranchUniversity(UUID branchId, List<UUID> universityIds) {
        //유효한 지부가 값으로 들어옴
        universityIds.forEach(universityId -> {
            if(!universityRepository.existsById(universityId)){ //유효한 대학인지 확인
                throw new BranchUniversityHandler(ErrorCode.UNIVERSITY_NOT_FOUND);
            }
            if (branchUniversityRepository.existsByBranchIdAndUniversityId(branchId, universityId)) { //지부-대학 연결 여부 확인
                throw new BranchUniversityHandler(ErrorCode.BRANCH_UNIVERSITY_ALREADY_EXIST); //이미 연결됨
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
    }

    //지부, 대학 연결 끊기
    @Transactional
    public void disconnectBranchUniversity(UUID branchId, List<UUID> universityIds) {
        //지부가 값으로 들어옴
        universityIds.forEach(universityId -> {
            if(!universityRepository.existsById(universityId)){ //유효한 대학인지 확인
                throw new BranchUniversityHandler(ErrorCode.UNIVERSITY_NOT_FOUND);
            }
            if (!branchUniversityRepository.existsByBranchIdAndUniversityId(branchId, universityId)) { //지부-대학 연결 여부 확인
                throw new BranchUniversityHandler(ErrorCode.BRANCH_UNIVERSITY_NOT_FOUND); //연결되지 않음
            }
            branchUniversityRepository.deleteByBranchIdAndUniversityId(branchId, universityId);
        });
    }

}
