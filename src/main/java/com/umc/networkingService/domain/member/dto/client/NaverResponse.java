package com.umc.networkingService.domain.member.dto.client;

import lombok.Getter;

@Getter
public class NaverResponse {
    private Response response;

    @Getter
    public static class Response {
        private String id;
    }
}
