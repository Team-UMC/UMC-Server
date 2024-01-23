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
    @AllArgsConstructor // 대학교 리스트 조회
    public static class joinUniversityList {
        List<UniversityList> universityList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 대학교 리스트
    public static class UniversityList {
        UUID universityId;
        String universityName;
    }
}
