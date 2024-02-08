package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BoardCommentService extends EntityLoader<BoardComment, UUID> {
    BoardCommentResponse.BoardCommentId addBoardComment(Member member, BoardCommentRequest.BoardCommentAddRequest request);

    BoardCommentResponse.BoardCommentId updateBoardComment(Member member, UUID commentId, BoardCommentRequest.BoardCommentUpdateRequest request);

    BoardCommentResponse.BoardCommentId deleteBoardComment(Member member, UUID commentId);

    BoardCommentResponse.BoardCommentPageInfos showBoardComments(Member member, UUID boardId, Pageable pageable);

    MyBoardResponse.MyBoardPageInfos showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable);

    MyBoardResponse.MyBoardCommentPageInfos showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

}

