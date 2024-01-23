package com.umc.networkingService.domain.university.dto.response;

import com.umc.networkingService.domain.branch.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class UniversityResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 대학교
    public static class joinUniversity {
        UUID universityId;
        String universityName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 별 세부 정보 조회
    public static class joinUniversityDetail {
        UUID universityId;
        String universityName;
        String universityLogo;
        Long universityPoint;
        Integer universityRank;

        public static joinUniversityDetail setUniversityRank(joinUniversityDetail universityDetail, Integer rank) {
            universityDetail.universityRank = rank;
            return universityDetail;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 랭킹
    public static class joinUniversityRank {
        String universityName;
        String universityLogo;
        Long universityPoint;
        Integer universityRank;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 랭킹 기여도
    public static class joinContributionRank {
        String nickname;
        String name;
        String profileImage;
        Long usedPoint;
        Integer rank; //todo: 랭킹 얼마까지? , 랭킹 패이징 해?
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 마스코트
    public static class joinUniversityMascot {
        Integer level;
        Long point;
        Integer rank;
        String mascotImage;
        String mascotDialog;
        String branchImage;

        public static joinUniversityMascot setRankAndBranch(joinUniversityMascot universityMascot, Integer rank, Branch branch) {
            universityMascot.rank = rank;
            universityMascot.branchImage = branch.getImage();
            return universityMascot;
        }
    }

}
