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
    public static class JoinUniversities {
        List<JoinUniversity> joinUniversities;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 대학교
    public static class JoinUniversity {
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
    @AllArgsConstructor
    public static class JoinUniversityRanks {
        List<JoinUniversityRank> joinUniversityRanks;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 랭킹
    public static class JoinUniversityRank {
        String universityName;
        String universityLogo;
        Long universityPoint;
        Integer universityRank;
        public static void setRank(JoinUniversityRank joinUniversityRank ,Integer newRank) {
            joinUniversityRank.universityRank = newRank;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 랭킹 기여도
    public static class JoinContributionRanks {
        List<JoinContributionRank> joinContributionRanks;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 학교 랭킹 기여도
    public static class JoinContributionRank {
        String nickname;
        String name;
        String profileImage;
        Long usedPoint;
        Integer rank;
        public static void setRank(JoinContributionRank joinContributionRank ,Integer newRank) {
            joinContributionRank.rank = newRank;
        }
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
        List<String> mascotDialog;
        String universityImage;

        public static joinUniversityMascot setRank(joinUniversityMascot universityMascot, Integer rank) {
            universityMascot.rank = rank;
            return universityMascot;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinUniversityMascotFeed {
        Long universityTotalpoint;
        Long memberRemainPoint;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UniversityId {
        UUID universityId;
    }

}
