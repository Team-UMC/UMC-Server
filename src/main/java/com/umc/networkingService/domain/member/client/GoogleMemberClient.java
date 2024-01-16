package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.GoogleResponse;
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
        System.out.println(accessToken);

        GoogleResponse response = webClient.get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleResponse.class)
                .block();

        //TODO 정보 받기 실패 예외 처리
        if(response ==null)
            return null;

        return response.getSub();
    }
}
