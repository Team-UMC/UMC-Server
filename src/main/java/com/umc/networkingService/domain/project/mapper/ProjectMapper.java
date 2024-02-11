package com.umc.networkingService.domain.project.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectMember;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProjectMapper {
    public Project toProject(String imageUrl, ProjectCreateRequest request) {
        return Project.builder()
                .logoImage(imageUrl)
                .name(request.getName())
                .slogan(request.getSlogan())
                .description(request.getDescription())
                .tags(request.getTags())
                .semester(request.getSemester())
                .type(request.getTypes())
                .build();
    }
    public ProjectDetailResponse detailProject(Project project){
        return ProjectDetailResponse.builder()
                .name(project.getName())
                .slogan(project.getSlogan())
                .description(project.getDescription())
                .type(project.getType())
                .build();
    }

    public ProjectMember toProjectMember(ProjectCreateRequest.ProjectMemberInfo memberInfo, Project project) {
        return ProjectMember.builder()
                .project(project)
                .nickname(memberInfo.getNickname())
                .name(memberInfo.getName())
                .part(memberInfo.getPart())
                .build();
    }
}
