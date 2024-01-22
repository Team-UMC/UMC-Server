package com.umc.networkingService.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class BoardCommentUpdateRequest {
    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;
}

