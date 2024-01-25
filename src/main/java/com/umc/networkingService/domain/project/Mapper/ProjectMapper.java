package com.umc.networkingService.domain.project.Mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest.CreateProject;
import com.umc.networkingService.domain.project.entity.Project;
import org.springframework.stereotype.Component;


@Component
public class ProjectMapper {
    public static Project createToProject(Member member, String imageUrl, CreateProject request) {
        return Project.builder()
                .logoImage(imageUrl)
                .name(request.getName())
                .slogan(request.getSlogan())
                .description(request.getDescription())

    }


}
