package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.KakaoResponse;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoMemberClient {

    private final WebClient webClient;


    public KakaoMemberClient(WebClient.Builder webClientBuilder ) {
        this.webClient = webClientBuilder
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build();
    }

    public String getkakaoClientID(final String accessToken) {
        KakaoResponse response = webClient.get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoResponse.class)
                .block();

        if(response == null)
            throw new RestApiException(ErrorCode._INTERNAL_SERVER_ERROR);

        return response.getId();
    }
}
