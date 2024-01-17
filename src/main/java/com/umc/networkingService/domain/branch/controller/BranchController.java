package com.umc.networkingService.domain.branch.controller;

import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.service.BranchService;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.validation.annotation.ExistBranch;
import com.umc.networkingService.validation.annotation.ExistUniversity;
import com.umc.networkingService.validation.annotation.ValidPage;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchUniversityService branchUniversityService;

    @Operation(summary = "지부 생성 API")
    @PostMapping("")
    public BaseResponse<String> postBranch(
            @RequestBody BranchRequest.PostBranchDTO request
    ){
        branchService.postBranch(request);
        return BaseResponse.onSuccess("지부 생성 완료");
    }

    @Operation(summary = "지부 수정 API")
    @PatchMapping("")
    public BaseResponse<String> patchBranch(
            @RequestBody BranchRequest.PatchBranchDTO request
    ){
        branchService.patchBranch(request);
        return BaseResponse.onSuccess("지부 수정 완료");
    }

    @Operation(summary = "지부 삭제 API")
    @DeleteMapping("")
    public BaseResponse<String> patchBranch(
            @ExistBranch @RequestParam("branchId") UUID branchId
    ){
        branchService.deleteBranch(branchId);
        return BaseResponse.onSuccess("지부 삭제 완료");
    }

    @Operation(summary = "지부 리스트 정보 조회 API")
    @PostMapping("")
    public BaseResponse<BranchResponse.JoinBranchListDTO> joinBranchList(
           @ValidPage @RequestParam(value = "page",defaultValue = "1") int page
    ){
        //기수별 조회 처리 있다면 추가 하기

        return BaseResponse.onSuccess(branchService.joinBranchList(page-1));
    }

    @Operation(summary = "지부 세부 정보 조회 API")
    @PostMapping("/detail")
    public BaseResponse<BranchResponse.JoinBranchDetailDTO> joinBranchDetail(
            @RequestParam("branchId") @ExistBranch UUID branchId
    ){
        return BaseResponse.onSuccess(branchService.joinBranchDetail(branchId));
    }

    @Operation(summary = "지부 대학교 연결 API")
    @PostMapping("/connection")
    public BaseResponse<String> connectBranch(
            @RequestParam("branchId") @ExistBranch UUID branchId,
            @RequestBody List<UUID> request //추후 뷰에 따라서 List가 아닌 단일 값으로 변경될 가능성 있음 (단일 값이라면 @RequestParam 으로 받음)
    ){
        branchUniversityService.connectBranchUniversity(branchId,request);
        return BaseResponse.onSuccess("지부 대학교 연결 완료");
    }

    @Operation(summary = "지부 대학교 연결 해제 API")
    @DeleteMapping("/connection")
    public BaseResponse<String> disconnectBranch(
            @RequestParam("branchId") @ExistBranch UUID branchId,
            @RequestParam("universityId") @ExistUniversity UUID universityId
    ){
        branchUniversityService.disconnectBranchUniversity(branchId,universityId);
        return BaseResponse.onSuccess("지부 대학교 연결 해제 완료");
    }

}
