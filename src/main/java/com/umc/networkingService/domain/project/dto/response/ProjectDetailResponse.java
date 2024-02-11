package com.umc.networkingService.domain.project.dto.response;

import com.umc.networkingService.domain.project.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.umc.networkingService.domain.member.entity.Member;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDetailResponse {
    private String name;
    private String slogan;
    private String description;
    private List<Member> members;
    private List<ProjectType> projectType;
}
