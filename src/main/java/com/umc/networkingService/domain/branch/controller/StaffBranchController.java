package com.umc.networkingService.domain.branch.controller;


import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.service.BranchServiceImpl;
import com.umc.networkingService.domain.branch.service.BranchUniversityServiceImpl;
import com.umc.networkingService.domain.branch.validation.annotation.ExistBranch;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    //todo: 다음 기수 생성하기?, 현재 진행중인 기수 바꾸기? api 추가하기 (현재 기수 isActive false로 만들기)

    @Operation(summary = "지부 생성 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BRANCH005", description = "지부 저장에 실패"),
            @ApiResponse(responseCode = "BRANCH002", description = "지부 이름이 비어있는 경우"),
            @ApiResponse(responseCode = "BRANCH003", description = "지부 설명이 비어있는 경우")
    })
    @PostMapping("")
    public BaseResponse<BranchResponse.BranchId> postBranch(
            @CurrentMember Member member,
            @RequestBody BranchRequest.BranchInfoDTO request
    ){
        return BaseResponse.onSuccess(branchService.postBranch(request));
    }

    @Operation(summary = "지부 수정 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BRANCH001", description = "수정할 지부를 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "BRANCH002", description = "지부 이름이 비어있는 경우"),
            @ApiResponse(responseCode = "BRANCH003", description = "지부 설명이 비어있는 경우")
    })
    @PatchMapping("/{branchId}")
    public BaseResponse<BranchResponse.BranchId> patchBranch(
            @CurrentMember Member member,
            @PathVariable("branchId") @ExistBranch UUID branchId,
            @RequestBody BranchRequest.BranchInfoDTO request
    ){
        return BaseResponse.onSuccess(branchService.patchBranch(request, branchId));
    }

    @Operation(summary = "지부 삭제 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @DeleteMapping("/{branchId}")
    public BaseResponse<BranchResponse.BranchId> patchBranch(
            @CurrentMember Member member,
            @Validated @ExistBranch @PathVariable("branchId") UUID branchId
    ){
        return BaseResponse.onSuccess(branchService.deleteBranch(branchId));
    }

    @Operation(summary = "지부 대학교 연결 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "UNIVERSITY001", description = "대학교를 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "BRANCH001", description = "지부를 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "BRANCH_UNIVERSITY002", description = "이미 연결된 지부와 대학교가 있는 경우")
    })
    @PostMapping("/connection")
    public BaseResponse<BranchResponse.ConnectBranch> connectBranch(
            @CurrentMember Member member,
            @RequestParam("branchId") @ExistBranch UUID branchId,
            @RequestBody BranchRequest.ConnectBranchDTO request
    ){
        return BaseResponse.onSuccess(branchUniversityService.connectBranchUniversity(branchId,request.getUniversityIds()));
    }

    @Operation(summary = "지부 대학교 연결 해제 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "UNIVERSITY001", description = "대학교를 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "BRANCH001", description = "지부를 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "BRANCH_UNIVERSITY001", description = "연결되지 않은 지부와 대학교가 있는 경우")
    })
    @DeleteMapping("/connection")
    public BaseResponse<BranchResponse.ConnectBranch> disconnectBranch(
            @CurrentMember Member member,
            @RequestParam("branchId") @ExistBranch UUID branchId,
            @RequestBody BranchRequest.ConnectBranchDTO request
    ){
        return BaseResponse.onSuccess(branchUniversityService.disconnectBranchUniversity(branchId, request.getUniversityIds()));
    }

}
