package com.umc.networkingService.domain.member.dto.client.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubAccessTokenResponse {
    private String accessToken;
}
