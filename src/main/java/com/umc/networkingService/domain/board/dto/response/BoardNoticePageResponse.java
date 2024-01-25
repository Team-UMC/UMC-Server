package com.umc.networkingService.domain.board.dto.response;

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
public class BoardNoticePageResponse {
    private UUID boardId;
    private HostType hostType;
    private String writer;
    private String title;
    private int hitCount;
    private boolean isFixed;
    private LocalDateTime createdAt;

}
