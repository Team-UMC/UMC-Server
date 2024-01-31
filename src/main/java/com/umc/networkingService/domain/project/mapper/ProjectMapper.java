package com.umc.networkingService.domain.project.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.entity.Project;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProjectMapper {
    public Project createToProject(String imageUrl, List<Member> members,
                                              ProjectCreateRequest request) {
        return Project.builder()
                .logoImage(imageUrl)
                .name(request.getName())
                .slogan(request.getSlogan())
                .description(request.getDescription())
                .members(members)
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
                .members(project.getMembers())
                .type(project.getType())
                .build();
    }

}
