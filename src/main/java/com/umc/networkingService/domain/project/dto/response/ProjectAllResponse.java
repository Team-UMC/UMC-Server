package com.umc.networkingService.domain.project.dto.response;

import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAllResponse {
    private List<ProjectInfo> projects;
    private int page;
    private int totalPages;
    private int totalElements;
    private Boolean isFirst;
    private Boolean isLast;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectInfo {
        private UUID projectId;
        private String name;
        private String description;
        private String logoImage;
        private Semester semester;
        private List<ProjectType> types;
        private List<String> tags;
    }
}
