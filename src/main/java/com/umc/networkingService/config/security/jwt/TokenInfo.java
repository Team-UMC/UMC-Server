package com.umc.networkingService.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class TokenInfo {

    private String accessToken;
    private String refreshToken;
}
