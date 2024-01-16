package com.umc.networkingService.domain.member.client;

import com.umc.networkingService.domain.member.dto.client.NaverResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverMemberClient {

    private final WebClient webClient;

    public NaverMemberClient(WebClient.Builder webClientBuilder){
        this.webClient=webClientBuilder
                .baseUrl("https://openapi.naver.com/v1/nid/me")
                .build();
    }

    public String getnaverClientID(final String accessToken){
        NaverResponse response=webClient.get()
                .header("Authorization","Bearer "+ accessToken)
                .retrieve()
                .bodyToMono(NaverResponse.class)
                .block();

        if(response != null)
            return response.getResponse().getId();
        return null;
    }
}
