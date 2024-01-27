package com.umc.networkingService.domain.branch.service;


import com.umc.networkingService.domain.branch.converter.BranchConverter;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.execption.BranchHandler;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.utils.S3FileComponent;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("Branch 서비스의 ")
@SpringBootTest
class BranchServiceTest extends ServiceIntegrationTestConfig {

    @Autowired
    private BranchServiceImpl branchService;

    @Autowired
    private BranchRepository branchRepository;

    @MockBean
    private S3FileComponent s3FileComponent;


    @Test
    @DisplayName("지부 생성 (이미지 없는 경우) - 성공")
    @Transactional
    void postBranch_Success() {
        // given
        BranchRequest.PostBranchDTO request = BranchRequest.PostBranchDTO.builder()
                .name("branchTestPost")
                .semester(Semester.FIRST)
                .image(null)
                .description("Branch Description")
                .build();

        // when
        UUID id = branchService.postBranch(request);

        // then
        Optional<Branch> optionalBranch = branchRepository.findById(id);
        assertTrue(optionalBranch.isPresent());
        Branch newBranch=optionalBranch.get();

        assertEquals(request.getName(), newBranch.getName());
        assertEquals(request.getDescription(), newBranch.getDescription());

        branchRepository.deleteById(id);
    }

    @Test
    @DisplayName("지부 수정 (이미지 있는 경우) - 성공")
    @Transactional
    void patchBranch_Success_img() {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "branch.png",
                "image/png",
                "이미지 데이터".getBytes()
        );
        // 테스트에서는 s3 파일 생성 X
        given(s3FileComponent.uploadFile(any(), any())).willReturn("s3 url");

        BranchRequest.PatchBranchDTO request =BranchRequest.PatchBranchDTO.builder()
                .branchId(branch.getId())
                .name("Updated Branch")
                .description("Updated Description")
                .image(image)
                .semester(Semester.FIFTH)
                .build();

        // when
        branchService.patchBranch(request);

        // then
        Optional<Branch> optionalBranch = branchRepository.findById(branch.getId());
        assertTrue(optionalBranch.isPresent());
        Branch updatedBranch=optionalBranch.get();

        assertEquals(request.getName(), updatedBranch.getName());
        assertEquals(request.getDescription(), updatedBranch.getDescription());

    }


    @Test
    @DisplayName("지부 수정 (이미지 없는 경우) - 성공")
    @Transactional
    void patchBranch_Success() {
        // given
        BranchRequest.PatchBranchDTO request =BranchRequest.PatchBranchDTO.builder()
                .branchId(branch.getId())
                .name("Updated Branch")
                .description("Updated Description")
                .image(null)
                .semester(Semester.FIFTH)
                .build();

        // when
        branchService.patchBranch(request);

        // then
        Optional<Branch> optionalBranch = branchRepository.findById(branch.getId());
        assertTrue(optionalBranch.isPresent());
        Branch updatedBranch=optionalBranch.get();

        assertEquals(request.getName(), updatedBranch.getName());
        assertEquals(request.getDescription(), updatedBranch.getDescription());

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
    @DisplayName("지부 리스트 조회 - 성공")
    @Transactional(readOnly = true)
    void joinBranchList_Success() {

        Semester semester = branch.getSemester();
        System.out.println("Test Semester: " + semester); //로그

        // when
        BranchResponse.JoinBranchListDTO result = branchService.joinBranchList(semester);

        // then
        List<BranchResponse.BranchDTO> branchList = result.getBranchList();

        System.out.println("Actual Branch List Size: " + branchList.size()); // 로그
        System.out.println("Expected Branch List Size: " + branchRepository.findAllBySemester(semester).size()); // 로그
        System.out.println("Actual Branch List: " + branchList); // 로그
        System.out.println("Expected Branch List: " + branchRepository.findAllBySemester(semester)); // 로그

        List<Branch> branchs = branchRepository.findAllBySemester(semester);

        assertEquals(branchs.size(), branchList.size());

    }

    @Test
    @DisplayName("지부 상세 조회 - 성공")
    @Transactional(readOnly = true)
    void joinBranchDetail_Success() {
        // given


        // when
        BranchResponse.JoinBranchDetailDTO result = branchService.joinBranchDetail(branch.getId());

        // then
        List<BranchUniversity> branchUniversities = branchUniversityRepository.findAllByBranch(branch);

        System.out.println("Actual Branch University List Size: " + branchUniversities.size()); // 로그
        System.out.println("Expected Branch University List Size: " + result.getUniversities()); // 로그
        assertEquals(branchUniversities.size(), result.getUniversities().size());

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

    @Test
    @DisplayName("지부 삭제 - 성공")
    @Transactional
    void deleteBranch_Success() {
        // given

        // when
        branchService.deleteBranch(branch.getId());

        // then
        assertFalse(branchRepository.existsById(branch.getId()));
    }

    @Test
    @DisplayName("지부 삭제 - 존재하지 않는 지부로 실패")
    @Transactional
    void deleteBranch_NonExisting_Failure() {
        // given
        UUID nonExistingBranchId = UUID.randomUUID();


        // when & then
        assertThrows(BranchHandler.class, () -> branchService.deleteBranch(nonExistingBranchId));
    }

}
