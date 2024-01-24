package com.umc.networkingService.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentPagingResponse {
    private List<BoardCommentPageResponse> boardCommentPageResponses = new ArrayList<>();
    private int page;
    private int totalPages;
    private int totalElements;
    private Boolean isFirst;
    private Boolean isLast;
}
