package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardResponse.BoardPageInfos;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.request.BoardCommentRequest.BoardCommentAddRequest;
import static com.umc.networkingService.domain.board.dto.request.BoardCommentRequest.BoardCommentUpdateRequest;
import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.*;
import static com.umc.networkingService.domain.board.dto.response.BoardResponse.MyBoardCommentPageElement;
import static com.umc.networkingService.domain.board.dto.response.BoardResponse.MyBoardPageElement;

public interface BoardCommentService extends EntityLoader<BoardComment, UUID> {
    BoardCommentId addBoardComment(Member member, UUID commentId,BoardCommentAddRequest request);

    BoardCommentId updateBoardComment(Member member, UUID commentId, BoardCommentUpdateRequest request);

    BoardCommentId deleteBoardComment(Member member, UUID commentId);

    BoardCommentPageInfos<BoardCommentPageElement> showBoardComments(Member member, UUID boardId, Pageable pageable);

    BoardPageInfos<MyBoardPageElement> showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable);

    BoardCommentPageInfos<MyBoardCommentPageElement> showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);
}

