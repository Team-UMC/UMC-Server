package com.umc.networkingService.domain.board.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentIdResponse {
    private UUID commentId;
}
