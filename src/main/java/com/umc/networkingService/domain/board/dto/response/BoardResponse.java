package com.umc.networkingService.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardResponse {
    @Getter
    @Builder
    public static class BoardDetail {
        private HostType hostType;
        private BoardType boardType;
        private String writer;
        private String profileImage;
        private Part part;
        private Semester semester;
        private String title;
        private String content;
        private List<String> boardFiles;
        private int hitCount;
        private int heartCount;
        private int commentCount;
        private Boolean isLiked;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
        private Boolean isFixed;
    }

    @Getter
    @SuperBuilder
    public static class BoardSearchPageElement extends BoardPageElement {
        private HostType hostType;
        private BoardType boardType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardPageInfos {
        private List<BoardPageElement> boardPageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardSearchPageInfos {
        private List<BoardSearchPageElement> boardSearchPageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NoticePageInfos {
        private List<NoticePageElement> noticePageElements = new ArrayList<>();
        private int page;
        private int totalPages;
        private int totalElements;
        private Boolean isFirst;
        private Boolean isLast;
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class PinnedNotice {
        private UUID boardId;
        private String title;
        private String content;
        private HostType hostType;
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PinnedNotices {
        List<PinnedNotice> pinnedNotices = new ArrayList<>();
    }

}
