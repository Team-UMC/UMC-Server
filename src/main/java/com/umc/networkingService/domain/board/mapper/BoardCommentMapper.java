package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentPageResponse;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentPagingResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardCommentPageWebResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardCommentPagingWebResponse;
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
    public BoardComment toEntity(Member member, Board board, BoardCommentAddRequest request) {
        return BoardComment.builder()
                .writer(member)
                .content(request.getContent())
                .board(board)
                .build();
    }

    public BoardCommentPagingResponse toBoardCommentPagingResponse(Page<BoardComment> comments) {
        List<BoardCommentPageResponse> responses = comments.map(this::toBoardCommentPageResponse)
                .stream().toList();
        return BoardCommentPagingResponse.builder()
                .boardCommentPageResponses(responses)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public BoardCommentPageResponse toBoardCommentPageResponse(BoardComment comment) {
        return BoardCommentPageResponse.builder()
                .commentId(comment.getId())
                .writer(comment.getWriter().getNickname() + "/" + comment.getWriter().getName())
                .profileImage(comment.getWriter().getProfileImage())
                .part(comment.getWriter().getRecentPart())
                .semester(comment.getWriter().getRecentSemester())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public MyBoardCommentPagingWebResponse toMyBoardCommentPagingWebResponse(Page<BoardComment> comments) {
        List<MyBoardCommentPageWebResponse> responses = comments.map(this::toMyBoardCommentPageWebResponse)
                .stream().toList();
        return MyBoardCommentPagingWebResponse.builder()
                .myBoardCommentPageWebResponses(responses)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public MyBoardCommentPageWebResponse toMyBoardCommentPageWebResponse(BoardComment comment) {
        return MyBoardCommentPageWebResponse.builder()
                .boardId(comment.getBoard().getId())
                .hostType(comment.getBoard().getHostType())
                .boardType(comment.getBoard().getBoardType())
                .title(comment.getBoard().getTitle())
                .comment(comment.getContent())
                .commentCreatedAt(comment.getCreatedAt())
                .build();
    }


}
