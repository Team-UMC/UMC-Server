package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentPageElement;
import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentPageInfos;
import static com.umc.networkingService.domain.board.dto.response.MyBoardResponse.MyBoardCommentPageElement;
import static com.umc.networkingService.domain.board.dto.response.MyBoardResponse.MyBoardCommentPageInfos;

@Component
public class BoardCommentMapper {
    public BoardComment toEntity(Member member, Board board, BoardCommentRequest.BoardCommentAddRequest request) {
        return BoardComment.builder()
                .writer(member)
                .content(request.getContent())
                .board(board)
                .build();
    }

    public BoardCommentPageInfos toBoardCommentPageInfos(Page<BoardComment> comments, List<BoardCommentPageElement> commentPageElements) {

        return BoardCommentPageInfos.builder()
                .boardCommentPageElements(commentPageElements)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public BoardCommentPageElement toBoardCommentPageElement(BoardComment comment, boolean isMine) {
        return BoardCommentPageElement.builder()
                .commentId(comment.getId())
                .writer(comment.getWriter().getNickname() + "/" + comment.getWriter().getName())
                .profileImage(comment.getWriter().getProfileImage())
                .part(comment.getWriter().getRecentPart())
                .semester(comment.getWriter().getRecentSemester())
                .content(comment.getContent())
                .isMine(isMine)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public MyBoardCommentPageInfos toMyBoardCommentPageInfos(Page<BoardComment> comments) {
        List<MyBoardCommentPageElement> commentPageElements = comments.map(this::toMyBoardCommentPageElement)
                .stream().toList();
        return MyBoardCommentPageInfos.builder()
                .myBoardCommentPageElement(commentPageElements)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements((int) comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

    public MyBoardCommentPageElement toMyBoardCommentPageElement(BoardComment comment) {
        return MyBoardCommentPageElement.builder()
                .boardId(comment.getBoard().getId())
                .hostType(comment.getBoard().getHostType())
                .boardType(comment.getBoard().getBoardType())
                .title(comment.getBoard().getTitle())
                .comment(comment.getContent())
                .commentCreatedAt(comment.getCreatedAt())
                .build();
    }




}
