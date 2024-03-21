package com.umc.networkingService.domain.branch.dto.request;

import com.umc.networkingService.global.common.enums.Semester;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class BranchRequest {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class BranchInfoDTO{
        @NotNull
        private String name; //todo: 글자수 제한 .. 중복 검증도?
        @NotNull
        private String description; //todo: 글자수 제한
        @NotNull
        private Semester semester;

    }

    @Getter
    public static class ConnectBranchDTO{

        @NotNull
        private List<UUID> universityIds;
    }
}
