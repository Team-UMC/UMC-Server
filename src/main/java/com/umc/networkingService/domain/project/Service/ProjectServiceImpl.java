package com.umc.networkingService.domain.project.Service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.project.Mapper.ProjectMapper;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest.CreateProject;
import com.umc.networkingService.domain.project.dto.response.ProjectCreateResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectCreateResponse.ProjectId;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final S3FileComponent s3FileComponent;
    private final MemberService memberService;

    @Override
    public ProjectCreateResponse createProject(Member member, MultipartFile projectImage, ProjectCreateRequest request) {

        // 프로젝트 이미지 S3 저장
        String imageUrl = s3FileComponent.uploadFile("project", projectImage);

        request.getMembers()

        return null;
    }
}
