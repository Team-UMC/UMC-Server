package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.AppleResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AppleMemberClient {
    private WebClient webClient;

    public AppleMemberClient(WebClient.Builder webclientBuilder){
        this.webClient = webclientBuilder
                .baseUrl("https://appleid.apple.com/auth/keys")
                .build();
    }

    public String getappleClientID(final String accessToken){
        AppleResponse response = webClient.get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(AppleResponse.class)
                .block();

        //TODO 정보 받기 실패 예외 처리
        if(response == null)
            return null;

        return response.getSub();
    }
}
