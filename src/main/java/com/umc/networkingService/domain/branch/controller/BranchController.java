package com.umc.networkingService.domain.branch.controller;

import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.service.BranchService;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchUniversityService branchUniversityService;

    @Operation(summary = "지부 생성 API")
    @PostMapping("")
    public BaseResponse<String> postBranch(@RequestBody @Valid BranchRequest.PostBranchDTO request){
        branchService.postBranch(request);
        return BaseResponse.onSuccess("지부 생성 완료");
    }

    @Operation(summary = "지부 수정 API")
    @PatchMapping("")
    public BaseResponse<String> patchBranch(){
    }

    @Operation(summary = "지부 삭제 API")
    @DeleteMapping("")
    public BaseResponse<String> patchBranch(){
    }

    @Operation(summary = "지부 정보 조회 API")
    @PostMapping("")
    public BaseResponse<BranchResponse.JoinBranchDTO> joinBranch(){
    }
    @Operation(summary = "지부 대학교 연결 API")
    @PostMapping("/connection")
    public BaseResponse<String> linkBranch() {
    }

}
