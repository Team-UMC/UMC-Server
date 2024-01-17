package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchUniversityHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.validation.annotation.ExistUniversity;
import lombok.RequiredArgsConstructor;
import org.attoparser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchUniversityService {

    private final BranchRepository branchRepository;
    private final UniversityRepository universityRepository;
    private final BranchUniversityRepository branchUniversityRepository;

    //지부, 대학 연결하기
    @Transactional
    public void connectBranchUniversity(UUID branchId, List<UUID> request) {

        request.forEach(universityId -> {
            if(!isUniversityValid(universityId)){ //유효한 대학인지 확인
                throw new BranchUniversityHandler(ErrorCode.UNIVERSITY_NOT_FOUND);
            }
            if (isBranchUniversityExist(branchId, universityId)) { //지부-대학 연결 여부 확인
                throw new BranchUniversityHandler(ErrorCode.BRANCH_UNIVERSITY_ALREADY_EXIST);
            }
            branchUniversityRepository.save
                    (
                    BranchUniversity.builder()
                    .branch(branchRepository.findById(branchId).get())
                    .university(universityRepository.findById(universityId).get())
                    .build()
            );
        });
    }

    //지부, 대학 연결 끊기
    @Transactional
    public void disconnectBranchUniversity(UUID branchId, UUID universityId) {
        //유효한 대학, 지부가 값으로 들어옴
        if (!isBranchUniversityExist(branchId, universityId)) { //지부-대학 연결 여부 확인
            throw new BranchUniversityHandler(ErrorCode.BRANCH_UNIVERSITY_NOT_FOUND);
        }
        branchUniversityRepository.deleteByBranchIdAndUniversityId(branchId, universityId);
    }

    //지부-대학 연결 여부 확인
    @Transactional(readOnly = true)
    public boolean isBranchUniversityExist(UUID branchId, UUID universityId) {
        return branchUniversityRepository.existsByBranchIdAndUniversityId(branchId, universityId);
    }

    //유효한 대학인지 확인
    @Transactional(readOnly = true)
    public boolean isUniversityValid(UUID universityId) {
        return universityRepository.existsById(universityId);
    }

}
