package com.umc.networkingService.domain.branch.controller;


import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.service.BranchServiceImpl;
import com.umc.networkingService.domain.branch.service.BranchUniversityServiceImpl;
import com.umc.networkingService.domain.branch.validation.annotation.ExistBranch;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Semester;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "운영진용 지부 API", description = "운영진용 지부 관련 API")

@RestController
@Validated
@RequestMapping("/staff/branch")
@RequiredArgsConstructor
public class StaffBranchController {

    private final BranchServiceImpl branchService;
    private final BranchUniversityServiceImpl branchUniversityService;

    @Operation(summary = "지부 생성 API")
    @PostMapping("")
    public BaseResponse<UUID> postBranch(
            @CurrentMember Member member,
            @RequestBody BranchRequest.PostBranchDTO request
    ){
        return BaseResponse.onSuccess(branchService.postBranch(request));
    }

    @Operation(summary = "지부 수정 API")
    @PatchMapping("")
    public BaseResponse<UUID> patchBranch(
            @CurrentMember Member member,
            @RequestBody BranchRequest.PatchBranchDTO request
    ){
        return BaseResponse.onSuccess(branchService.patchBranch(request));
    }

    @Operation(summary = "지부 삭제 API")
    @DeleteMapping("/{branchId}")
    public BaseResponse<String> patchBranch(
            @CurrentMember Member member,
            @Validated @ExistBranch @PathVariable("branchId") UUID branchId
    ){
        branchService.deleteBranch(branchId);
        return BaseResponse.onSuccess("지부 삭제 완료");
    }

    @Operation(summary = "지부 대학교 연결 API")
    @PostMapping("/connection")
    public BaseResponse<String> connectBranch(
            @CurrentMember Member member,
            @RequestParam("branchId") @ExistBranch UUID branchId,
            @RequestBody BranchRequest.ConnectBranchDTO request
    ){
        branchUniversityService.connectBranchUniversity(branchId,request.getUniversityIds());
        return BaseResponse.onSuccess("지부 대학교 연결 완료");
    }

    @Operation(summary = "지부 대학교 연결 해제 API")
    @DeleteMapping("/connection")
    public BaseResponse<String> disconnectBranch(
            @CurrentMember Member member,
            @RequestParam("branchId") @ExistBranch UUID branchId,
            @RequestBody BranchRequest.ConnectBranchDTO request
    ){
        branchUniversityService.disconnectBranchUniversity(branchId, request.getUniversityIds());
        return BaseResponse.onSuccess("지부 대학교 연결 해제 완료");
    }

}
