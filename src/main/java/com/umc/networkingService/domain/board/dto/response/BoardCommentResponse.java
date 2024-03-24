package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private WriterInfo writerInfo;
        private String content;
        private Boolean isMine;
        private String createdAt;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardCommentPageInfos<T> {
        private List<T> boardCommentPageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
