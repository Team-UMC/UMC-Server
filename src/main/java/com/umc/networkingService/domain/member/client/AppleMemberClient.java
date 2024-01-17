package com.umc.networkingService.domain.member.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AppleMemberClient {
    private final WebClient webClient;

    public AppleMemberClient (WebClient.Builder webClientBuilder){
        this.webClient=webClientBuilder
                .baseUrl("https://appleid.apple.com/auth/keys")
                .build();
    }
    // 애플 로그인은 미완성, 나중에 추가로 구현할 예정
    
}
