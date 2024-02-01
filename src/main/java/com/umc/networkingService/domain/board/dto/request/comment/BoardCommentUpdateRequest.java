package com.umc.networkingService.domain.board.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentUpdateRequest {
    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;
}

