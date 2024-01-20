package com.umc.networkingService.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInquiryHomeInfoResponse {
    private String profileImage;
    private String nickname;
    private Long contributionPoint;
    private int contributionRank;
}
