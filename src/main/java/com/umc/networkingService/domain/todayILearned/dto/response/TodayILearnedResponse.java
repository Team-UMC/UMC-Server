package com.umc.networkingService.domain.todayILearned.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TodayILearnedResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TodayILearnedId {
        private UUID todayILearnedId;
    }
}
