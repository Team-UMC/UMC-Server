package com.umc.networkingService.domain.project.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.service.ProjectService;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.global.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public BaseResponse<ProjectIdResponse> createProject(@CurrentMember Member member,
                                                         @RequestPart MultipartFile projectImage,
                                                         @Valid @RequestPart ProjectCreateRequest request){
        return BaseResponse.onSuccess(projectService.createProject(member, projectImage, request));
    }
    @PostMapping("/update/{projectId}")
    public BaseResponse<ProjectIdResponse> updateProject(@CurrentMember Member member,
                                                             @PathVariable ("projectId") UUID projectId,
                                                             @RequestPart MultipartFile projectImage,
                                                             @Valid @RequestPart ProjectUpdateRequest request){
        return BaseResponse.onSuccess(projectService.updateProject(member, projectId, projectImage, request));
    }

    @DeleteMapping("/{projectId}")
    public BaseResponse<ProjectIdResponse> deleteProject(@PathVariable("projectId") UUID projectId){
        return BaseResponse.onSuccess(projectService.deleteProject(projectId));
    }

    @GetMapping
    public BaseResponse<ProjectIdResponse> searchProject(@PathVariable("projectName") String projectName){
        return BaseResponse.onSuccess(projectService.searchProject(projectName));
    }

    @GetMapping
    public BaseResponse<ProjectDetailResponse> detailProject(@PathVariable ("projectId") UUID projectId){
        return BaseResponse.onSuccess(projectService.detailProject(projectId));
    }
}
