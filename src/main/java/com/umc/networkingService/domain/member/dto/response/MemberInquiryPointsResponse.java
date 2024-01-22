package com.umc.networkingService.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberInquiryPointsResponse {
    private Long remainPoint;
    private List<UsedHistory> usedHistories;

    @Getter
    @Builder
    public static class UsedHistory {
        private String pointImage;
        private Long point;
        private String description;
    }
}
