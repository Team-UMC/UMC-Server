package com.umc.networkingService.domain.project.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.domain.project.service.ProjectService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "운영진용 Project API", description = "운영진용 Project 관련 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class StaffProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 등록 API", description = "프로젝트를 등록하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "IMAGE001", description = "이미지 저장에 실패하였습니다.")
    })
    @PostMapping
    public BaseResponse<ProjectIdResponse> createProject(@CurrentMember Member member,
                                                         @RequestPart MultipartFile projectImage,
                                                         @Valid @RequestPart ProjectCreateRequest request){
        return BaseResponse.onSuccess(projectService.createProject(member, projectImage, request));
    }

    @Operation(summary = "프로젝트 수정 API", description = "프로젝트를 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다."),
            @ApiResponse(responseCode = "PROJECT002", description = "해당 프로젝트에 대해 수정 권한이 없습니다.")
    })
    @PostMapping("/update/{projectId}")
    public BaseResponse<ProjectIdResponse> updateProject(@CurrentMember Member member,
                                                         @PathVariable("projectId") UUID projectId,
                                                         @RequestPart MultipartFile projectImage,
                                                         @Valid @RequestPart ProjectUpdateRequest request){
        return BaseResponse.onSuccess(projectService.updateProject(member, projectId, projectImage, request));
    }

    @Operation(summary = "프로젝트 삭제 API", description = "프로젝트를 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다."),
            @ApiResponse(responseCode = "PROJECT003", description = "해당 프로젝트에 대해 삭제 권한이 없습니다.")
    })
    @DeleteMapping("/{projectId}")
    public BaseResponse<ProjectIdResponse> deleteProject(@PathVariable("projectId") UUID projectId){
        return BaseResponse.onSuccess(projectService.deleteProject(projectId));
    }
}
