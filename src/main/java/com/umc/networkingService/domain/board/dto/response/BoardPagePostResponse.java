package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPagePostResponse {
    private HostType hostType;
    private BoardType boardType;
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
