package com.umc.networkingService.domain.university.dto.response;

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
    }


}
