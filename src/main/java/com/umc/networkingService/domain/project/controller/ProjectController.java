package com.umc.networkingService.domain.project.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.service.ProjectService;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Project API", description = "Project 관련 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "프로젝트 조회 API", description = "프로젝트를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다.")
    })
    public BaseResponse<ProjectIdResponse> searchProject(@PathVariable("projectName") String projectName){
        return BaseResponse.onSuccess(projectService.searchProject(projectName));
    }

    @Operation(summary = "프로젝트 상세 조회 API", description = "프로젝트를 상세 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다.")
    })
    @GetMapping
    public BaseResponse<ProjectDetailResponse> detailProject(@PathVariable ("projectId") UUID projectId){
        return BaseResponse.onSuccess(projectService.detailProject(projectId));
    }
}
