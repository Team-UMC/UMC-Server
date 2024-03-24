package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.dto.response.BoardResponse.*;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardFileService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.converter.DataConverter;
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

    public PinnedNotice toPinnedNotice(Board board) {
        return PinnedNotice.builder()
                .hostType(board.getHostType())
                .title(board.getTitle())
                .boardId(board.getId())
                .content(board.getContent())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .hitCount(board.getHitCount())
                .createdAt(DataConverter.convertToRelativeTimeFormat(board.getCreatedAt()))
                .build();
    }

    public PinnedNotices toPinnedNotices(List<Board> boards) {
        List<PinnedNotice> pinnedNotices = boards.stream().map(this::toPinnedNotice).toList();
        return PinnedNotices.builder()
                .pinnedNotices(pinnedNotices)
                .build();
    }

    public BoardPageElement toBoardPageElement(Board board) {
        return BoardPageElement.builder()
                .boardId(board.getId())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .title(board.getTitle())
                .content(board.getContent())
                .thumbnail(boardFileService.findThumbnailImage(board))
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createdAt(DataConverter.convertToRelativeTimeFormat(board.getCreatedAt()))
                .isFixed(false)
                .build();
    }


    public BoardSearchPageElement toBoardSearchPageElement(Board board) {
        return BoardSearchPageElement.builder()
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
                .createdAt(DataConverter.convertToRelativeTimeFormat(board.getCreatedAt()))
                .isFixed(false)
                .build();
    }

    public MyBoardPageElement toMyBoardPageElement(Board board) {
        return MyBoardPageElement.builder()
                .boardId(board.getId())
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .title(board.getTitle())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .createdAt(DataConverter.convertToRelativeTimeFormat(board.getCreatedAt()))
                .build();
    }

    public NoticePageElement toNoticePageElement(Board board) {
        return NoticePageElement.builder()
                .boardId(board.getId())
                .hostType(board.getHostType())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .title(board.getTitle())
                .hitCount(board.getHitCount())
                .isFixed(board.isFixed())
                .createdAt(DataConverter.convertToRelativeTimeFormat(board.getCreatedAt()))
                .build();
    }

    public BoardDetail toBoardDetail(Board board, List<String> boardFiles, boolean isLiked, boolean isMine) {
        return BoardDetail.builder()
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
                .isMine(isMine)
                .createdAt(DataConverter.convertToRelativeTimeFormat(board.getCreatedAt()))
                .build();
    }

    public <T> BoardPageInfos<T> toBoardPageInfos(Page<Board> boards, List<T> boardPageElements) {
        return BoardPageInfos.<T>builder()
                .boardPageElements(boardPageElements)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();

    }

}
