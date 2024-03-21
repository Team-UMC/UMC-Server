package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
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

    public Board toEntity(Member member, BoardRequest.BoardCreateRequest request,
                          List<Semester> semesterPermission) {
        return Board.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .hostType(HostType.valueOf(request.getHostType()))
                .boardType(BoardType.valueOf(request.getBoardType()))
                .semesterPermission(semesterPermission)
                .build();
    }

    public BoardResponse.PinnedNotice toPinnedNotice(Board board) {
        return BoardResponse.PinnedNotice.builder()
                .hostType(board.getHostType())
                .title(board.getTitle())
                .boardId(board.getId())
                .content(board.getContent())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .hitCount(board.getHitCount())
                .createdAt(board.getCreatedAt())
                .build();
    }
    public BoardResponse.PinnedNotices toPinnedNotices(List<Board> boards) {
        List<BoardResponse.PinnedNotice> pinnedNotices = boards.stream().map(this::toPinnedNotice).toList();
        return BoardResponse.PinnedNotices.builder()
                .pinnedNotices(pinnedNotices)
                .build();
    }
    public BoardResponse.BoardPageElement toBoardPageElement(Board board) {
        return BoardResponse.BoardPageElement.builder()
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
                .isFixed(false)
                .build();
    }

    public BoardResponse.BoardPageInfos toBoardPageInfos(Page<Board> boards) {

        List<BoardResponse.BoardPageElement> boardPageElements = boards.map(this::toBoardPageElement).stream().toList();
        return BoardResponse.BoardPageInfos.builder()
                .boardPageElements(boardPageElements)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();

    }

    public BoardResponse.BoardSearchPageInfos toBoardSearchPageInfos(Page<Board> boards) {

        List<BoardResponse.BoardSearchPageElement> boardSearchPageElements = boards.map(this::toBoardSearchPageElement).stream().toList();
        return BoardResponse.BoardSearchPageInfos.builder()
                .boardSearchPageElements(boardSearchPageElements)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();

    }


    public BoardResponse.BoardSearchPageElement toBoardSearchPageElement(Board board) {
        return BoardResponse.BoardSearchPageElement.builder()
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
                .isFixed(false)
                .build();
    }

    public MyBoardResponse.MyBoardPageInfos toMyBoardPageInfos(Page<Board> boards) {
        List<MyBoardResponse.MyBoardPageElement> myBoardPageElements = boards.map(this::toMyBoardPageElement).stream().toList();

        return MyBoardResponse.MyBoardPageInfos.builder()
                .myBoardPageElements(myBoardPageElements)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();
    }

    public MyBoardResponse.MyBoardPageElement toMyBoardPageElement(Board board) {
        return  MyBoardResponse.MyBoardPageElement.builder()
                .boardId(board.getId())
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .title(board.getTitle())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardResponse.NoticePageElement toNoticePageElement(Board board) {
        return  BoardResponse.NoticePageElement.builder()
                .boardId(board.getId())
                .hostType(board.getHostType())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .title(board.getTitle())
                .hitCount(board.getHitCount())
                .isFixed(board.isFixed())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardResponse.NoticePageInfos toBoardNoticePagingResponse(Page<Board> boards) {
        List<BoardResponse.NoticePageElement> noticePageElements = boards.map(this::toNoticePageElement).stream().toList();

        return BoardResponse.NoticePageInfos.builder()
                .noticePageElements(noticePageElements)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();
    }

    public BoardResponse.BoardDetail toBoardDetail(Board board, List<String> boardFiles, boolean isLiked) {
        return BoardResponse.BoardDetail.builder()
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
