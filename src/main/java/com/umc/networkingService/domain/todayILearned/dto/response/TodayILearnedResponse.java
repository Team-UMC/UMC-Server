package com.umc.networkingService.domain.todayILearned.dto.response;

import com.umc.networkingService.global.common.enums.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class TodayILearnedResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TodayILearnedId {
        private UUID todayILearnedId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayILearnedInfo {
        private UUID todayILearnedId;
        private String title;
        private String subTitle;
        private Part part;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayILearnedInfos {
        private List<TodayILearnedInfo> todayILearnedInfos;
    }

}
