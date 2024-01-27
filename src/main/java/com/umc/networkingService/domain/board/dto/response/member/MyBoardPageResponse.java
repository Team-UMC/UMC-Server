package com.umc.networkingService.domain.board.dto.response.member;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class MyBoardPageResponse {
    private UUID boardId;
    private HostType hostType;
    private BoardType boardType;
    private String title;
    private int hitCount;
    private int heartCount;
    private LocalDateTime createdAt;
}
