package com.umc.networkingService.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String toKorean;
}
