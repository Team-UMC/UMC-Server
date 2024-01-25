package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchUniversityHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class BranchUniversityServiceTest {

    @InjectMocks
    private BranchUniversityService branchUniversityService;

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
        List<UUID> universityIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        given(branchRepository.findById(branchId)).willReturn(Optional.of(new Branch()));
        given(universityRepository.existsById(any(UUID.class))).willReturn(true);
        given(branchUniversityRepository.existsByBranchIdAndUniversityId(eq(branchId), any(UUID.class))).willReturn(false);

        // when
        branchUniversityService.connectBranchUniversity(branchId, universityIds);

        // then
        verify(branchRepository).findById(branchId);
        verify(universityRepository, times(2)).existsById(any(UUID.class));
        verify(branchUniversityRepository, times(2)).existsByBranchIdAndUniversityId(eq(branchId), any(UUID.class));
        verify(branchUniversityRepository, times(2)).save(any(BranchUniversity.class));
    }

    @Test
    @DisplayName("지부, 대학 연결 - 실패 (유효하지 않은 대학)")
    void connectBranchUniversity_InvalidUniversity_Failure() {
        // given
        UUID branchId = UUID.randomUUID();
        List<UUID> invalidUniversityIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        given(branchRepository.findById(branchId)).willReturn(Optional.of(new Branch()));
        given(universityRepository.existsById(any(UUID.class))).willReturn(false);

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

        given(branchRepository.findById(branchId)).willReturn(Optional.of(new Branch()));
        given(universityRepository.existsById(any(UUID.class))).willReturn(true);
        given(branchUniversityRepository.existsByBranchIdAndUniversityId(eq(branchId), eq(connectedUniversityId))).willReturn(true);

        // when & then
        assertThrows(BranchUniversityHandler.class, () -> branchUniversityService.connectBranchUniversity(branchId, universityIds));
        verify(branchRepository).findById(branchId);
        verify(universityRepository, times(2)).existsById(any(UUID.class));
        verify(branchUniversityRepository, times(2)).existsByBranchIdAndUniversityId(eq(branchId), any(UUID.class));
        verify(branchUniversityRepository, never()).save(any(BranchUniversity.class));
    }

}

