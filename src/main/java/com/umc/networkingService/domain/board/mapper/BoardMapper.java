package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.*;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPageResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPagingResponse;
import com.umc.networkingService.domain.board.dto.response.notice.BoardNoticePageResponse;
import com.umc.networkingService.domain.board.dto.response.notice.BoardNoticePagingResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.service.BoardFileService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final BoardFileService boardFileService;

    public Board toEntity(Member member, BoardCreateRequest request,
                          List<Semester> semesterPermission) {
        return Board.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .hostType(request.getHostType())
                .boardType(request.getBoardType())
                .semesterPermission(semesterPermission)
                .build();
    }

    public BoardPageResponse toBoardPageResponse(Board board) {
        return BoardPageResponse.builder()
                .boardId(board.getId())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .title(board.getTitle())
                .content(board.getContent())
                .thumbnail(boardFileService.findThumbnailImage(board))
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardPagingResponse toBoardPagingResponse(Page<Board> boards) {

        List<BoardPageResponse> BoardPageResponses = boards.map(this::toBoardPageResponse).stream().toList();
        return BoardPagingResponse.builder()
                .boardPageResponses(BoardPageResponses)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();

    }

    public BoardSearchPagingResponse toBoardSearchPagingResponse(Page<Board> boards) {

        List<BoardSearchPageResponse> boardSearchPageResponses = boards.map(this::toBoardSearchPageResponse).stream().toList();
        return BoardSearchPagingResponse.builder()
                .boardSearchPageResponses(boardSearchPageResponses)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();

    }


    public BoardSearchPageResponse toBoardSearchPageResponse(Board board) {
        return BoardSearchPageResponse.builder()
                .boardType(board.getBoardType())
                .hostType(board.getHostType())
                .boardId(board.getId())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .title(board.getTitle())
                .content(board.getContent())
                .thumbnail(boardFileService.findThumbnailImage(board))
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public MyBoardPagingResponse toMyBoardPagingResponse(Page<Board> boards) {
        List<MyBoardPageResponse> myBoardPageResponses = boards.map(this::toMyBoardPageResponse).stream().toList();

        return MyBoardPagingResponse.builder()
                .myBoardPageResponses(myBoardPageResponses)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();
    }

    public MyBoardPageResponse toMyBoardPageResponse(Board board) {
        return MyBoardPageResponse.builder()
                .boardId(board.getId())
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .title(board.getTitle())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardNoticePageResponse toBoardNoticePageResponse(Board board) {
        return BoardNoticePageResponse.builder()
                .boardId(board.getId())
                .hostType(board.getHostType())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .title(board.getTitle())
                .hitCount(board.getHitCount())
                .isFixed(board.isFixed())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardNoticePagingResponse toBoardNoticePagingResponse(Page<Board> boards) {
        List<BoardNoticePageResponse> boardNoticePageResponses = boards.map(this::toBoardNoticePageResponse).stream().toList();

        return BoardNoticePagingResponse.builder()
                .boardNoticePageResponses(boardNoticePageResponses)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();
    }

    public BoardDetailResponse toBoardDetailResponse(Board board, List<String> boardFiles, boolean isLiked) {
        return BoardDetailResponse.builder()
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .part(board.getWriter().getRecentPart())
                .semester(board.getWriter().getRecentSemester())
                .title(board.getTitle())
                .content(board.getContent())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .boardFiles(boardFiles)
                .isLiked(isLiked)
                .createdAt(board.getCreatedAt())
                .build();
    }

}
