package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
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
public class BoardPageCommentResponse {
    private UUID commentId;
    private String writer;
    private String profileImage;
    private Semester semester;
    private Part part;
    private String content;
    private LocalDateTime createdAt;
}

