package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.request.BoardCommentRequest.*;
import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentId;
import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentPageInfos;
import static com.umc.networkingService.domain.board.dto.response.MyBoardResponse.MyBoardCommentPageInfos;
import static com.umc.networkingService.domain.board.dto.response.MyBoardResponse.MyBoardPageInfos;

public interface BoardCommentService extends EntityLoader<BoardComment, UUID> {
    BoardCommentId addBoardComment(Member member, UUID commentId,BoardCommentAddRequest request);

    BoardCommentId updateBoardComment(Member member, UUID commentId, BoardCommentUpdateRequest request);

    BoardCommentId deleteBoardComment(Member member, UUID commentId);

    BoardCommentPageInfos showBoardComments(Member member, UUID boardId, Pageable pageable);

    MyBoardPageInfos showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable);

    MyBoardCommentPageInfos showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

    boolean isMyComment(BoardComment boardComment, Member member);

}

