package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardResponse {
    @Getter
    @Builder
    public static class BoardDetail {
        private HostType hostType;
        private BoardType boardType;
        private WriterInfo writerInfo;
        private String title;
        private String content;
        private List<String> boardFiles;
        private int hitCount;
        private int heartCount;
        private int commentCount;
        private Boolean isLiked;
        private Boolean isMine;
        private String createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class BoardId {
        private UUID boardId;
    }

    @Getter
    @SuperBuilder
    public static class BoardPageElement {
        private UUID boardId;
        private String writer;
        private String profileImage;
        private String title;
        private String content;
        private String thumbnail;
        private int hitCount;
        private int heartCount;
        private int commentCount;
        private String createdAt;
        private Boolean isFixed;
    }

    @Getter
    @SuperBuilder
    public static class BoardSearchPageElement extends BoardPageElement {
        private HostType hostType;
        private BoardType boardType;
    }

    @Getter
    @Builder
    public static class NoticePageElement {
        private UUID boardId;
        private HostType hostType;
        private String writer;
        private String title;
        private int hitCount;
        private Boolean isFixed;
        private String createdAt;
    }

    @Getter
    @Builder
    public static class MyBoardPageElement {
        private UUID boardId;
        private HostType hostType;
        private BoardType boardType;
        private String title;
        private int hitCount;
        private int heartCount;
        private String createdAt;
    }

    @Getter
    @Builder
    public static class MyBoardCommentPageElement {
        private UUID boardId;
        private HostType hostType;
        private BoardType boardType;
        private String title;
        private String comment;
        private String commentCreatedAt;
    }

    @Getter
    @Builder
    public static class PinnedNotice {
        private UUID boardId;
        private String title;
        private String content;
        private HostType hostType;
        private int hitCount;
        private String writer;
        private String createdAt;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PinnedNotices {
        List<PinnedNotice> pinnedNotices = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardPageInfos<T> {
        private List<T> boardPageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
