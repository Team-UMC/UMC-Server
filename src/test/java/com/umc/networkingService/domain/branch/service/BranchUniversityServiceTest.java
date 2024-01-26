package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchUniversityHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.Semester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class BranchUniversityServiceTest {

    @InjectMocks
    private BranchUniversityServiceImpl branchUniversityService;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UniversityRepository universityRepository;

    @Mock
    private BranchUniversityRepository branchUniversityRepository;

    @Test
    @DisplayName("지부, 대학 연결 - 성공")
    void connectBranchUniversity_Success() {
        // given
        UUID branchId = UUID.randomUUID();
        Branch branch = new Branch(branchId, "branch connect test", "branch description", null, Semester.FIRST);

        University university1 = new University(UUID.randomUUID(), null,"", "", "university1", 100L);
        University university2 = new University(UUID.randomUUID(), null, "", "", "university2",80L);
        List<UUID> universityIds = List.of(university1.getId(), university2.getId());

        // when
        branchUniversityService.connectBranchUniversity(branchId, universityIds);

        // then
        BranchUniversity branchUniversity = branchUniversityRepository.findByBranchIdAndUniversityId(branchId, university1.getId());
        assertEquals(2, branchUniversityRepository.findByBranch(branch).size());
        assertEquals(branchId, branchUniversity.getBranch().getId());
        assertEquals(university1.getId(), branchUniversity.getUniversity().getId());
    }

    @Test
    @DisplayName("지부, 대학 연결 - 실패 (유효하지 않은 대학)")
    void connectBranchUniversity_InvalidUniversity_Failure() {
        // given
        UUID branchId = UUID.randomUUID();
        List<UUID> invalidUniversityIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        // when & then
        assertThrows(BranchUniversityHandler.class, () -> branchUniversityService.connectBranchUniversity(branchId, invalidUniversityIds));
        verify(branchRepository).findById(branchId);
        verify(universityRepository, times(2)).existsById(any(UUID.class));
        verify(branchUniversityRepository, never()).existsByBranchIdAndUniversityId(any(UUID.class), any(UUID.class));
        verify(branchUniversityRepository, never()).save(any(BranchUniversity.class));
    }

    @Test
    @DisplayName("지부, 대학 연결 - 실패 (이미 연결된 대학)")
    void connectBranchUniversity_AlreadyConnectedUniversity_Failure() {
        // given
        UUID branchId = UUID.randomUUID();
        UUID connectedUniversityId = UUID.randomUUID();
        List<UUID> universityIds = List.of(connectedUniversityId, UUID.randomUUID());

        // when & then
        assertThrows(BranchUniversityHandler.class, () -> branchUniversityService.connectBranchUniversity(branchId, universityIds));
        verify(branchRepository).findById(branchId);
        verify(universityRepository, times(2)).existsById(any(UUID.class));
        verify(branchUniversityRepository, times(2)).existsByBranchIdAndUniversityId(eq(branchId), any(UUID.class));
        verify(branchUniversityRepository, never()).save(any(BranchUniversity.class));
    }

}

