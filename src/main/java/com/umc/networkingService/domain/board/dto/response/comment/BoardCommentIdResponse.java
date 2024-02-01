package com.umc.networkingService.domain.board.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BoardCommentIdResponse {
    private UUID commentId;
}
