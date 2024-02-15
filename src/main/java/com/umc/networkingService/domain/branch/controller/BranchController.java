package com.umc.networkingService.domain.branch.controller;

import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.service.BranchServiceImpl;
import com.umc.networkingService.domain.branch.validation.annotation.ExistBranch;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Semester;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "지부 API", description = "지부 관련 API")

@RestController
@Validated
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchServiceImpl branchService;

    @Operation(summary = "지부 리스트 정보 조회 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "SEMESTER001", description = "잘못된 기수 값이 입력된 경우")
    })
    @GetMapping("")
    public BaseResponse<BranchResponse.JoinBranchs> joinBranchList(
            @RequestParam("semester") Semester semester //기수별로 조회
    ){
        return BaseResponse.onSuccess(branchService.joinBranchList(semester));
    }

    @Operation(summary = "지부 세부 정보 조회 API")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BRANCH001", description = "지부를 찾을 수 없는 경우")
    })
    @GetMapping("/detail")
    public BaseResponse<BranchResponse.JoinBranchDetails> joinBranchDetail(
            @RequestParam("branchId") @Validated @ExistBranch UUID branchId
    ){
        return BaseResponse.onSuccess(branchService.joinBranchDetail(branchId));
    }

}
