package com.umc.networkingService.domain.project.mapper;

import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectAllResponse;
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
                .description(request.getDescription())
                .tags(request.getTags())
                .semester(request.getSemester())
                .projectType(request.getProjectTypes())
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

    public ProjectAllResponse.ProjectInfo toProjectInfo(Project project) {
        return ProjectAllResponse.ProjectInfo.builder()
                .projectId(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .logoImage(project.getLogoImage())
                .semester(project.getSemester())
                .types(project.getProjectType())
                .tags(project.getTags())
                .build();
    }

    public ProjectDetailResponse toProjectDetailResponse(Project project, List<ProjectMember> projectMembers) {
        return ProjectDetailResponse.builder()
                .name(project.getName())
                .description(project.getDescription())
                .logoImage(project.getLogoImage())
                .semester(project.getSemester())
                .types(project.getProjectType())
                .tags(project.getTags())
                .projectMembers(toProjectMemberInfos(projectMembers))
                .build();
    }

    private List<ProjectDetailResponse.ProjectMemberInfo> toProjectMemberInfos(List<ProjectMember> projectMembers) {
        return projectMembers.stream()
                .map(this::toProjectMemberInfo)
                .toList();
    }

    private ProjectDetailResponse.ProjectMemberInfo toProjectMemberInfo(ProjectMember projectMember) {
        return ProjectDetailResponse.ProjectMemberInfo.builder()
                .part(projectMember.getPart())
                .nickname(projectMember.getNickname())
                .name(projectMember.getName())
                .build();
    }
}
