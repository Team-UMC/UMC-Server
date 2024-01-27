package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchUniversityHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("BranchUniversity 서비스의 ")
@SpringBootTest
class BranchUniversityServiceTest extends ServiceIntegrationTestConfig {

    @Autowired
    private BranchUniversityServiceImpl branchUniversityService;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private BranchUniversityRepository branchUniversityRepository;


    @Test
    @DisplayName("지부, 대학 연결 - 성공")
    @Transactional
    void connectBranchUniversity_Success() {
        // given
        List<UUID> universityIds = List.of(notLinkedBranchUniversity.getId());

        // when
        branchUniversityService.connectBranchUniversity(branch.getId(), universityIds);

        // then
        BranchUniversity branchUniversity = branchUniversityRepository.findByBranchAndUniversity(branch, notLinkedBranchUniversity);
        assertEquals(branch.getId(), branchUniversity.getBranch().getId());
        assertEquals(notLinkedBranchUniversity.getId(), branchUniversity.getUniversity().getId());
    }

    @Test
    @DisplayName("지부, 대학 연결 - 실패 (유효하지 않은 대학)")
    @Transactional
    void connectBranchUniversity_InvalidUniversity_Failure() {
        // given
        UUID branchId = UUID.randomUUID();
        List<UUID> invalidUniversityIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        // when & then
        assertThrows(BranchUniversityHandler.class, () -> branchUniversityService.connectBranchUniversity(branchId, invalidUniversityIds));
    }

    @Test
    @DisplayName("지부, 대학 연결 - 실패 (이미 연결된 대학)")
    @Transactional
    void connectBranchUniversity_AlreadyConnectedUniversity_Failure() {
        // given
        List<UUID> universityIds = List.of(university.getId());

        // when & then
        assertThrows(BranchUniversityHandler.class, () -> branchUniversityService.connectBranchUniversity(branch.getId(), universityIds));
    }

}

