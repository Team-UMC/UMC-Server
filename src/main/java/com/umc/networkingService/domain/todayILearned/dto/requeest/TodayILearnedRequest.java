package com.umc.networkingService.domain.todayILearned.dto.requeest;

import com.umc.networkingService.global.common.enums.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TodayILearnedRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TodayILearnedCreate {
        private Part part;
        private String title;
        private String subTitle;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TodayILearnedUpdate {
        private String title;
        private String subTitle;
        private String content;
        private Part part;
    }
}
