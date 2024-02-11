package com.umc.networkingService.domain.project.dto.request;

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
public class ProjectUpdateRequest {
    @NotEmpty(message = "프로젝트명은 공백일 수 없습니다.")
    private String name;
    private String description;
    @Size(max = 3, message = "프로젝트 태그는 최대 3개까지 입니다.")
    private List<String> tags;
}
