package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardCommentPagingResponse;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BoardCommentService  extends EntityLoader<BoardComment, UUID> {
    BoardCommentIdResponse addBoardComment(Member member, BoardCommentAddRequest request);
    BoardCommentIdResponse updateBoardComment(Member member, UUID commentId, BoardCommentUpdateRequest request);
    BoardCommentIdResponse deleteBoardComment(Member member, UUID commentId);
    BoardCommentPagingResponse showBoardComments(Member member, UUID boardId, Pageable pageable);
    }
