package com.umc.networkingService.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class BoardCommentRequest {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardCommentAddRequest {
        @NotNull(message = "boardId는 필수 입력값입니다.")
        private UUID boardId;
        @NotBlank(message = "content는 필수 입력값입니다.")
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardCommentReplyRequest {
        @NotNull(message = "commentId는 필수 입력값입니다.")
        private UUID commentId;
        @NotBlank(message = "content는 필수 입력값입니다.")
        private String content;
    }



    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardCommentUpdateRequest {
        @NotBlank(message = "content는 필수 입력값입니다.")
        private String content;
    }
}
