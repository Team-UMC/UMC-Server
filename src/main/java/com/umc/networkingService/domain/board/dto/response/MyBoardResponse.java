package com.umc.networkingService.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyBoardResponse {
    @Getter
    @Builder
    public static class MyBoardPageElement {
        private UUID boardId;
        private HostType hostType;
        private BoardType boardType;
        private String title;
        private int hitCount;
        private int heartCount;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyBoardPageInfos {
        private List<MyBoardPageElement> myBoardPageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Getter
    @Builder
    public static class MyBoardCommentPageElement {
        private UUID boardId;
        private HostType hostType;
        private BoardType boardType;
        private String title;
        private String comment;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime commentCreatedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyBoardCommentPageInfos {
        private List<MyBoardCommentPageElement> myBoardCommentPageElement = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
