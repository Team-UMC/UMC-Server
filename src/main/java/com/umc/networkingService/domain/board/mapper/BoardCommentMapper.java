package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardCommentMapper {
    public BoardComment toEntity(Member member, Board board, BoardCommentRequest.BoardCommentAddRequest request) {
        return BoardComment.builder()
                .writer(member)
                .content(request.getContent())
                .board(board)
                .build();
    }

    public BoardCommentResponse.BoardCommentPageInfos toBoardCommentPageInfos(Page<BoardComment> comments) {
        List<BoardCommentResponse.BoardCommentPageElement> responses = comments.map(this::toBoardCommentPageElement)
                .stream().toList();
        return BoardCommentResponse.BoardCommentPageInfos.builder()
                .boardCommentPageElements(responses)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public BoardCommentResponse.BoardCommentPageElement toBoardCommentPageElement(BoardComment comment) {
        return BoardCommentResponse.BoardCommentPageElement.builder()
                .commentId(comment.getId())
                .writer(comment.getWriter().getNickname() + "/" + comment.getWriter().getName())
                .profileImage(comment.getWriter().getProfileImage())
                .part(comment.getWriter().getRecentPart())
                .semester(comment.getWriter().getRecentSemester())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public MyBoardResponse.MyBoardCommentPageInfos toMyBoardCommentPageInfos(Page<BoardComment> comments) {
        List<MyBoardResponse.MyBoardCommentPageElement> responses = comments.map(this::toMyBoardCommentPageElement)
                .stream().toList();
        return MyBoardResponse.MyBoardCommentPageInfos.builder()
                .myBoardCommentPageElement(responses)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public MyBoardResponse.MyBoardCommentPageElement toMyBoardCommentPageElement(BoardComment comment) {
        return MyBoardResponse.MyBoardCommentPageElement.builder()
                .boardId(comment.getBoard().getId())
                .hostType(comment.getBoard().getHostType())
                .boardType(comment.getBoard().getBoardType())
                .title(comment.getBoard().getTitle())
                .comment(comment.getContent())
                .commentCreatedAt(comment.getCreatedAt())
                .build();
    }


}
