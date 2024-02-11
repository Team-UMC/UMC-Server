package com.umc.networkingService.domain.project.dto.response;

import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.umc.networkingService.domain.member.entity.Member;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDetailResponse {
    private String name;
    private String description;
    private String logoImage;
    private Semester semester;
    private List<ProjectType> types;
    private List<String> tags;
    private List<ProjectMemberInfo> projectMembers;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectMemberInfo {
        private Part part;
        private String nickname;
        private String name;
    }
}
