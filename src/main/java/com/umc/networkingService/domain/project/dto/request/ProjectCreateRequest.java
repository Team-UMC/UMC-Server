package com.umc.networkingService.domain.project.dto.request;

import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.validation.annotation.ExistProjectName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectCreateRequest {
    @NotEmpty(message = "프로젝트명은 공백일 수 없습니다.")
    @ExistProjectName
    private String name;
    private String description;
    @Size(max = 3, message = "프로젝트 태그는 최대 3개까지 입니다.")
    private List<String> tags;
    @Size(min = 1, message = "최소 한 개의 타입을 지정해야 합니다.")
    private List<ProjectType> types;
    private Semester semester;
    private List<ProjectMemberInfo> projectMembers;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectMemberInfo {
        private String nickname;
        private String name;
        private Part part;
    }
}
