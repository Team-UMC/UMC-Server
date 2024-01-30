package com.umc.networkingService.domain.branch.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.service.BranchServiceImpl;
import com.umc.networkingService.domain.branch.service.BranchUniversityServiceImpl;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.branch.validation.annotation.ExistBranch;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Semester;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "지부 API", description = "지부 관련 API")

@RestController
@Validated
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchServiceImpl branchService;
    private final BranchUniversityServiceImpl branchUniversityService;

    @Operation(summary = "지부 리스트 정보 조회 API")
    @GetMapping("")
    public BaseResponse<BranchResponse.JoinBranchs> joinBranchList(
            @CurrentMember Member member,
            @RequestParam("semester") Semester semester //기수별로 조회해서, 페이징 생략
    ){
        return BaseResponse.onSuccess(branchService.joinBranchList(semester));
    }

    @Operation(summary = "지부 세부 정보 조회 API")
    @GetMapping("/detail")
    public BaseResponse<BranchResponse.JoinBranchDetails> joinBranchDetail(
            @CurrentMember Member member,
            @RequestParam("branchId") @ExistBranch UUID branchId
    ){
        return BaseResponse.onSuccess(branchService.joinBranchDetail(branchId));
    }

}
