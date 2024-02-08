package com.umc.networkingService.domain.branch.dto.response;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class BranchResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinBranchs{
        List<JoinBranch> branchs;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinBranch{
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
    public static class JoinBranchDetails{
        //todo: 뷰애 맞춰 고치기
        List<JoinBranchDetail> universities;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinBranchDetail{
        UUID universityId;
        String name;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BranchId{
        UUID branchId;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConnectBranch{
        UUID branchId;
        List<UUID> universityIds;
    }

}
