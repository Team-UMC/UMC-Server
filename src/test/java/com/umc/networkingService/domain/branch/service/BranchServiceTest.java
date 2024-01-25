package com.umc.networkingService.domain.branch.service;


import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.Semester;
import com.umc.networkingService.global.utils.S3FileComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UniversityRepository universityRepository;

    @Mock
    private BranchUniversityRepository branchUniversityRepository;

    @Mock
    private S3FileComponent s3FileComponent;

    @Test
    @DisplayName("지부 생성 - 성공")
    @Transactional
    void postBranch_Success() {
        // given
        BranchRequest.PostBranchDTO request = BranchRequest.PostBranchDTO.builder()
                .name("post branch test")
                .semester(Semester.FIRST)
                .description("Branch Description")
                .image(null)
                .build();

        // when
        UUID id = branchService.postBranch(request);

        // then
        assertEquals(request.getName(), branchRepository.findById(id).get().getName());
        assertEquals(request.getDescription(), branchRepository.findById(id).get().getDescription());
    }

    @Test
    @DisplayName("지부 수정 - 성공")
    @Transactional
    void patchBranch_Success() {
        // given
        UUID branchId = UUID.randomUUID();
        BranchRequest.PatchBranchDTO request =BranchRequest.PatchBranchDTO.builder()
                .branchId(branchId)
                .name("Updated Branch")
                .description("Updated Description")
                .image(null)
                .build();

        Branch existingBranch = Branch.builder()
                .id(branchId)
                .name("Branch Name")
                .description("Branch Description")
                .semester(Semester.FIRST)
                .build();

        branchRepository.save(existingBranch);

        // when
        branchService.patchBranch(request);

        // then
        assertEquals(request.getName(), existingBranch.getName());
        assertEquals(request.getDescription(), existingBranch.getDescription());
    }

    @Test
    @DisplayName("지부 수정 - 존재하지 않는 지부로 실패")
    @Transactional
    void patchBranch_NonExisting_Failure() {
        // given
        BranchRequest.PatchBranchDTO request = BranchRequest.PatchBranchDTO.builder()
                .branchId(UUID.randomUUID())
                .name("Updated Branch")
                .image(null)
                .build();

        // when & then
        assertThrows(BranchHandler.class, () -> branchService.patchBranch(request));
    }

    @Test
    @DisplayName("지부 삭제 - 성공")
    @Transactional
    void deleteBranch_Success() {
        // given
        UUID branchId = UUID.randomUUID();
        Branch existingBranch =
                Branch.builder()
                        .id(branchId)
                        .name("delete branch")
                        .description("")
                        .image(null)
                        .semester(Semester.FIRST)
                        .build();
        branchRepository.save(existingBranch);

        // when
        branchService.deleteBranch(branchId);

        // then
        verify(branchRepository).findById(branchId);
        verify(branchRepository).delete(existingBranch);
        assertNotEquals(null, existingBranch.getDeletedAt());
    }

    @Test
    @DisplayName("지부 삭제 - 존재하지 않는 지부로 실패")
    @Transactional
    void deleteBranch_NonExisting_Failure() {
        // given
        UUID nonExistingBranchId = UUID.randomUUID();


        // when & then
        assertThrows(BranchHandler.class, () -> branchService.deleteBranch(nonExistingBranchId));
        verify(branchRepository).findById(nonExistingBranchId);
        verify(branchRepository, never()).delete(any());
    }

    @Test
    @DisplayName("지부 리스트 조회 - 성공")
    @Transactional(readOnly = true)
    void joinBranchList_Success() {
        // given
        Semester semester = Semester.FIFTH;
        Branch branch1 =
                Branch.builder()
                        .image(null)
                        .name("join branch list 1")
                        .description("")
                        .semester(Semester.FIFTH)
                        .build();

        Branch branch2 =
                Branch.builder()
                        .image(null)
                        .name("join branch list 2")
                        .description("")
                        .semester(Semester.FIFTH)
                        .build();
        List<Branch> branches = List.of(
                branch1
                , branch2
        );
        branchRepository.saveAll(branches);

        // when
        BranchResponse.JoinBranchListDTO result = branchService.joinBranchList(semester);

        // then
        assertEquals(branches.size(), result.getBranchList().size());
        assertEquals(branches.get(0).getName(), result.getBranchList().get(0).getName());
        assertEquals(branches.get(1).getName(), result.getBranchList().get(1).getName());
    }

    @Test
    @DisplayName("지부 상세 조회 - 성공")
    @Transactional(readOnly = true)
    void joinBranchDetail_Success() {
        // given
        UUID branchId = UUID.randomUUID();
        Branch branch =
                Branch.builder()
                        .id(branchId)
                        .name("join branch detail")
                        .description("")
                        .image(null)
                        .semester(Semester.FIRST)
                        .build();
        branchRepository.save(branch);

        University university =
                University.builder()
                        .name("join branch detail university")
                        .build();
        universityRepository.save(university);

        BranchUniversity branchUniversity = new BranchUniversity(UUID.randomUUID(),branch, university);
        branchUniversityRepository.save(branchUniversity);


        // when
        BranchResponse.JoinBranchDetailDTO result = branchService.joinBranchDetail(branchId);

        // then
        assertEquals(university.getName(), result.getUniversityList().get(0).getName());
        assertEquals(0, result.getUniversityList().size());
    }

    @Test
    @DisplayName("지부 상세 조회 - 실패 (존재하지 않는 지부)")
    @Transactional(readOnly = true)
    void joinBranchDetail_NonExistingBranch_Failure() {
        // given
        UUID nonExistingBranchId = UUID.randomUUID();

        // when & then
        assertThrows(BranchHandler.class, () -> branchService.joinBranchDetail(nonExistingBranchId));
    }
}
