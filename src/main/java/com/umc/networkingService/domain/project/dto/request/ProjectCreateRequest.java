package com.umc.networkingService.domain.project.dto.request;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.project.entity.Type;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.validation.annotation.ValidEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectCreateRequest {
    @NotEmpty(message = "프로젝트명은 공백일 수 없습니다.")
    private String name;
    private String slogan;
    private String description;
    @Size(max = 3, message = "프로젝트 태그는 최대 3개까지 입니다.")
    private List<String> tags;
    @Size(min = 1, message = "최소 한 개의 타입을 지정해야 합니다.")
    @ValidEnum(message = "옳지 않은 값입니다. IOS, AOS, WEB 중 하나 이상을 선택해주세요.",
            enumClass = Type.class)
    private List<Type> types;
    private Semester semester;
    private List<ProjectMemberInfo> projectMembers;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectMemberInfo {
        @NotEmpty(message = "닉네임은 공백일 수 없습니다.")
        private String nickname;
        @NotEmpty(message = "닉네임은 공백일 수 없습니다.")
        private String name;
        @ValidEnum(message = "옳지 않은 값입니다. PM, DESIGN, SPRING, NODE, ANDROID, WEB 중 하나를 선택해주세요.",
                enumClass = Part.class)
        private Part part;
    }
}
