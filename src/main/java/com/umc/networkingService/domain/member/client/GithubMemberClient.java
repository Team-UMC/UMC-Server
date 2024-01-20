package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.GithubResponse;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubMemberClient {
    private final WebClient webClient;

    public GithubMemberClient() {
        this.webClient = generateWebClient();
    }

    private WebClient generateWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com/user")
                .build();
    }

    public String getGithubNickname(final String accessToken) {
        GithubResponse response = webClient
                .get()
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GithubResponse.class)
                .block();

        if (response == null)
            throw new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION);

        return response.getLogin();
    }
}