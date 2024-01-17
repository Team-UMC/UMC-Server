package com.umc.networkingService.domain.branch.dto.response;

import com.umc.networkingService.global.common.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BranchResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinBranchListDTO{
        //todo: 뷰애 맞춰 고치기
        List<BranchDTO> branchList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchDTO{
        UUID branchId;
        String name;
        String description;
        String image;
        Semester semester;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinBranchDetailDTO{
        //todo: 뷰애 맞춰 고치기
        List<BranchUniversityDTO> universityList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchUniversityDTO{
        UUID universityId;
        String name;
    }

}
