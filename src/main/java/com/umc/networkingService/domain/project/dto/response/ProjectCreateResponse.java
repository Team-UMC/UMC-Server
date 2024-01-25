package com.umc.networkingService.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class ProjectCreateResponse {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectId{
        private UUID projectId;
    }
}
