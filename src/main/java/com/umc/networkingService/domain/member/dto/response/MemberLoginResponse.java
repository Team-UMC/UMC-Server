package com.umc.networkingService.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MemberLoginResponse {
    private UUID memberId;
    private String accessToken;
    private String refreshToken;
}
