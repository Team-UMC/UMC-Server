package com.umc.networkingService.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateMyProfileRequest {
    private String name;
    private String nickname;
    private String statusMessage;
}
