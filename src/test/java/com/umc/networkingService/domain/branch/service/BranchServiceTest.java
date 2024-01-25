package com.umc.networkingService.domain.branch.service;


import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.university.entity.University;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;

    @Mock
    private BranchRepository branchRepository;

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
                .name("Branch 1")
                .semester(Semester.FIRST)
                .description("Branch Description")
                .image(null)
                .build();
        given(branchRepository.save(any(Branch.class))).willReturn(new Branch());

        // when
        branchService.postBranch(request);

        // then
        verify(s3FileComponent).uploadFile("branch", request.getImage());//s3FileComponent.uploadFile()가 호출되었는지 확인
        verify(branchRepository).save(any(Branch.class)); //branchRepository.save()가 호출되었는지 확인
        //todo: branchRepository.save()가 호출되었을 때, Branch 객체가 잘 생성되었는지 확인
    }

    @Test
    @DisplayName("지부 수정 - 성공")
    @Transactional
    void patchBranch_Success() {
        // given
        BranchRequest.PatchBranchDTO request =BranchRequest.PatchBranchDTO.builder()
                .branchId(UUID.randomUUID())
                .name("Updated Branch")
                .image(null)
                .build();
        Branch existingBranch = new Branch(UUID.randomUUID(), "Branch Name", "", null,Semester.FIRST);

        given(branchRepository.findById(request.getBranchId())).willReturn(Optional.of(existingBranch));
        given(branchRepository.save(any(Branch.class))).willReturn(new Branch());

        // when
        branchService.patchBranch(request);

        // then
        verify(s3FileComponent).uploadFile("branch", request.getImage());
        verify(branchRepository).findById(request.getBranchId());
        verify(branchRepository).save(existingBranch);
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
        given(branchRepository.findById(request.getBranchId())).willReturn(Optional.empty());

        // when & then
        assertThrows(BranchHandler.class, () -> branchService.patchBranch(request));
        verify(s3FileComponent, never()).uploadFile("branch", request.getImage());
        verify(branchRepository).findById(request.getBranchId());
        verify(branchRepository, never()).save(any());
    }

    @Test
    @DisplayName("지부 삭제 - 성공")
    @Transactional
    void deleteBranch_Success() {
        // given
        UUID branchId = UUID.randomUUID();
        Branch existingBranch = new Branch(UUID.randomUUID(), "Branch Name", "", null,Semester.FIRST);

        given(branchRepository.findById(branchId)).willReturn(Optional.of(existingBranch));

        // when
        branchService.deleteBranch(branchId);

        // then
        verify(branchRepository).findById(branchId);
        verify(branchRepository).delete(existingBranch);
    }

    @Test
    @DisplayName("지부 삭제 - 존재하지 않는 지부로 실패")
    @Transactional
    void deleteBranch_NonExisting_Failure() {
        // given
        UUID nonExistingBranchId = UUID.randomUUID();

        given(branchRepository.findById(nonExistingBranchId)).willReturn(Optional.empty());

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
        Semester semester = Semester.FIRST;
        List<Branch> branches = List.of(new Branch(UUID.randomUUID(), "Branch Name1", "", null,Semester.FIRST)
                , new Branch(UUID.randomUUID(), "Branch Name2", "", null,Semester.FIRST););

        given(branchRepository.findBySemester(semester)).willReturn(branches);
        given(branchConverter.toJoinBranchListDTO(branches)).willReturn(new BranchResponse.JoinBranchListDTO(List.of()));

        // when
        BranchResponse.JoinBranchListDTO result = branchService.joinBranchList(semester);

        // then
        verify(branchRepository).findBySemester(semester);
        verify(branchConverter).toJoinBranchListDTO(branches);
        assertEquals(0, result.getBranches().size());
    }

    @Test
    @DisplayName("지부 상세 조회 - 성공")
    @Transactional(readOnly = true)
    void joinBranchDetail_Success() {
        // given
        UUID branchId = UUID.randomUUID();
        Branch branch = new Branch(UUID.randomUUID(), "Branch Name", "", null,Semester.FIRST);
        University university = new University(UUID.randomUUID(), null,"","", "University Name", 100L);
        BranchUniversity branchUniversity = new BranchUniversity(UUID.randomUUID(),branch, university);

        given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
        given(branchUniversityRepository.findByBranch(branch)).willReturn(branchUniversities);
        given(branchConverter.toJoinBranchDetailDTO(List.of())).willReturn(new BranchResponse.JoinBranchDetailDTO(List.of()));

        // when
        BranchResponse.JoinBranchDetailDTO result = branchService.joinBranchDetail(branchId);

        // then
        verify(branchRepository).findById(branchId);
        verify(branchUniversityRepository).findByBranch(branch);
        verify(branchConverter).toJoinBranchDetailDTO(List.of());
        assertEquals(0, result.getUniversities().size());
    }

    @Test
    @DisplayName("지부 상세 조회 - 실패 (존재하지 않는 지부)")
    @Transactional(readOnly = true)
    void joinBranchDetail_NonExistingBranch_Failure() {
        // given
        UUID nonExistingBranchId = UUID.randomUUID();

        given(branchRepository.findById(nonExistingBranchId)).willReturn(Optional.empty());

        // when & then
        assertThrows(BranchHandler.class, () -> branchService.joinBranchDetail(nonExistingBranchId));
        verify(branchRepository).findById(nonExistingBranchId);
        verify(branchUniversityRepository, never()).findByBranch(any());
        verify(branchConverter, never()).toJoinBranchDetailDTO(any());
    }
}
