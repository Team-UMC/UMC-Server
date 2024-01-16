package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.KakaoResponse;
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

        System.out.printf("준석" +response.getId());

        if(response != null)
            return response.getId();

        // TODO 정보 받기 실패 예외처리
        return null;
    }
}
