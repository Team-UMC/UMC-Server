package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BranchUniversity 서비스의 ")
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
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
        University notLinkedBranchUniversity = createUniversity("notLinkedBranchUniversity");
        List<UUID> universityIds = List.of(notLinkedBranchUniversity.getId());

        // when
        branchUniversityService.connectBranchUniversity(branch.getId(), universityIds);

        // then
        BranchUniversity branchUniversity = branchUniversityRepository.findByBranchAndUniversity(branch, notLinkedBranchUniversity);
        assertEquals(branch.getId(), branchUniversity.getBranch().getId());
        assertEquals(notLinkedBranchUniversity.getId(), branchUniversity.getUniversity().getId());
    }


}

