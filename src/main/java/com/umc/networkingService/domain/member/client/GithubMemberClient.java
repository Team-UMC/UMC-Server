package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.github.GithubAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.client.github.GithubInfoResponse;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubMemberClient {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @Value("${github.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient;

    public GithubMemberClient() {
        this.webClient = generateWebClient();
    }

    private WebClient generateWebClient() {
        return WebClient.builder()
                .baseUrl("https://github.com/login/oauth/access_token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getAccessToken(final String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);

        GithubAccessTokenResponse response = webClient
                .post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GithubAccessTokenResponse.class)
                .blockOptional()
                .orElseThrow(() -> new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION));

        if (response == null)
            throw new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION);

        return response.getAccessToken();
    }

    public String getGithubNickname(final String code) {
        String accessToken = getAccessToken(code);

        WebClient userWebClient = WebClient.builder()
                .baseUrl("https://api.github.com/user")
                .build();

        GithubInfoResponse response = userWebClient
                .get()
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GithubInfoResponse.class)
                .block();

        if (response == null)
            throw new RestApiException(ErrorCode.FAILED_SOCIAL_LOGIN);

        return response.getLogin();
    }
}