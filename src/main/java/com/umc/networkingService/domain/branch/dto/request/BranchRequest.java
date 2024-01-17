package com.umc.networkingService.domain.branch.dto.request;

import com.umc.networkingService.validation.annotation.ExistBranch;
import com.umc.networkingService.global.common.Semester;
import com.umc.networkingService.validation.annotation.ExistUniversity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class BranchRequest {


    @Getter
    public static class PostBranchDTO{
        @NotNull
        private String name;
        @NotNull
        private String description;
        private MultipartFile image;
        @NotNull
        private Semester semester;

    }

    @Getter
    public static class PatchBranchDTO{
        @ExistBranch
        private UUID branchId;
        @NotNull
        private String name;
        @NotNull
        private String description;
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
