package com.umc.networkingService.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberGenerateNewAccessTokenResponse {
    private String accessToken;
}
