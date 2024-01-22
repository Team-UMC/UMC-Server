package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentIdResponse;
import com.umc.networkingService.domain.member.entity.Member;

public interface BoardCommentService  {
    BoardCommentIdResponse addBoardComment(Member member, BoardCommentAddRequest request);

}
