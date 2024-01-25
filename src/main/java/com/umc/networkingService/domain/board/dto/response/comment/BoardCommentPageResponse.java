package com.umc.networkingService.domain.board.dto.response.comment;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentPageResponse {
    private UUID commentId;
    private String writer;
    private String profileImage;
    private Semester semester;
    private Part part;
    private String content;
    private LocalDateTime createdAt;
}

