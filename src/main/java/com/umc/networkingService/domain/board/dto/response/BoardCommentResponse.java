package com.umc.networkingService.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardCommentResponse {
    @Getter
    @AllArgsConstructor
    public static class BoardCommentId {
        private UUID commentId;
    }

    @Getter
    @Builder
    public static class BoardCommentPageElement {
        private UUID commentId;
        private String writer;
        private String profileImage;
        private Semester semester;
        private Part part;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardCommentPageInfos {
        private List<BoardCommentPageElement> boardCommentPageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
