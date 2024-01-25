package com.umc.networkingService.domain.project.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.Service.ProjectService;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectCreateResponse;
import com.umc.networkingService.global.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public BaseResponse<ProjectCreateResponse> createProject(@CurrentMember Member member,
                                                             @RequestPart MultipartFile projectImage,
                                                             @Valid @RequestPart ProjectCreateRequest request){
        return BaseResponse.onSuccess(projectService.createProject(member, projectImage, request));
    }
}
