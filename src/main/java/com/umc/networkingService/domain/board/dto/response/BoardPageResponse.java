package com.umc.networkingService.domain.board.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class BoardPageResponse {
    private UUID boardId;
    private String writer;
    private String profileImage;
    private String title;
    private String content;
    private String thumbnail;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private boolean isFixed;
}
