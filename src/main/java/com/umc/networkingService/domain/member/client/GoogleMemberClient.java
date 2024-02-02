package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.GoogleResponse;
import com.umc.networkingService.global.common.exception.code.AuthErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleMemberClient {
    private final WebClient webClient;

    public GoogleMemberClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder
                .baseUrl("https://www.googleapis.com/oauth2/v3/userinfo")
                .build();
    }

    public String getgoogleClientID(final String accessToken) {
        GoogleResponse response = webClient.get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleResponse.class)
                .block();

        if(response == null)
            throw new RestApiException(AuthErrorCode.FAILED_SOCIAL_LOGIN);

        return response.getSub();
    }
}
