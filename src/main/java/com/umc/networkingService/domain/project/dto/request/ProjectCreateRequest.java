package com.umc.networkingService.domain.project.dto.request;

import com.umc.networkingService.domain.project.entity.Type;
import com.umc.networkingService.global.common.enums.Semester;
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
    private String name;
    private String slogan;
    private String description;
    private List<UUID> members;
    private List<String> tags;
    private List<Type> types;
    private Semester semester;
}
