package com.umc.networkingService.domain.branch.dto.request;

import com.umc.networkingService.domain.branch.validation.annotation.ExistBranch;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class BranchRequest {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class PostBranchDTO{
        @NotNull
        private String name; //todo: 글자수 제한 .. 중복 검증도?
        @NotNull
        private String description; //todo: 글자수 제한
        private MultipartFile image;
        @NotNull
        private Semester semester;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PatchBranchDTO{
        @ExistBranch
        private UUID branchId;
        @NotNull
        private String name; //todo: 글자수 제한 .. 중복 검증도?
        @NotNull
        private String description; //todo: 글자수 제한
        private MultipartFile image;
        @NotNull
        private Semester semester;

    }

    @Getter
    public static class ConnectBranchDTO{

        @NotNull
        private List<UUID> universityIdList;
    }
}
