package com.umc.networkingService.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPagePostResponse {
    private String writer;
    private String profileImage;
    private String title;
    private String content;
    private String thumbnail;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private LocalDateTime createdAt;
}
