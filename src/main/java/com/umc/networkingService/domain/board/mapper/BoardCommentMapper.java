package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentPagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPageCommentResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardCommentMapper {

    public BoardComment toEntity(Member member, Board board, BoardCommentAddRequest request) {
        return BoardComment.builder()
                .writer(member)
                .content(request.getContent())
                .board(board)
                .build();
    }

    public BoardCommentPagingResponse toBoardCommentPagingResponse(Page<BoardComment> comments) {
        List<BoardPageCommentResponse> responses = comments.map(this::toBoardPageCommentResponse)
                .stream().toList();
        return BoardCommentPagingResponse.builder()
                .boardPageCommentResponses(responses)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public BoardPageCommentResponse toBoardPageCommentResponse(BoardComment comment) {
        return BoardPageCommentResponse.builder()
                .commentId(comment.getId())
                .writer(comment.getWriter().getNickname() + "/" + comment.getWriter().getName())
                .profileImage(comment.getWriter().getProfileImage())
                .part(comment.getWriter().getRecentPart())
                .semester(comment.getWriter().getRecentSemester())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
