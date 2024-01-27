package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentIdResponse;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentPagingResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardCommentPagingWebResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPagingResponse;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BoardCommentService extends EntityLoader<BoardComment, UUID> {
    BoardCommentIdResponse addBoardComment(Member member, BoardCommentAddRequest request);

    BoardCommentIdResponse updateBoardComment(Member member, UUID commentId, BoardCommentUpdateRequest request);

    BoardCommentIdResponse deleteBoardComment(Member member, UUID commentId);

    BoardCommentPagingResponse showBoardComments(Member member, UUID boardId, Pageable pageable);
    MyBoardPagingResponse showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable);
    MyBoardCommentPagingWebResponse showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

}

