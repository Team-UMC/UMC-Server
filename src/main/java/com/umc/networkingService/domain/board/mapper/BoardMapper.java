package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagePostResponse;
import com.umc.networkingService.domain.board.entity.*;
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
    public Board toEntity(Member member, String title, String content, HostType hostType, BoardType boardType,
                                 List<Semester> semesterPermission) {
        return Board.builder()
                .writer(member)
                .title(title)
                .content(content)
                .hostType(hostType)
                .boardType(boardType)
                .semesterPermission(semesterPermission)
                .build();
    }

    public BoardPagePostResponse toBoardPagePostResponse(Board board) {
        return BoardPagePostResponse.builder()
                .boardId(board.getId())
                .writer(board.getWriter().getNickname()+"/"+board.getWriter().getName())
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

        List<BoardPagePostResponse> boardPagePostResponses = boards.map(this::toBoardPagePostResponse).stream().toList();
        return BoardPagingResponse.builder()
                .boardPagePostResponses(boardPagePostResponses)
                .page(boards.getNumber())
                .totalPages(boards.getTotalPages())
                .totalElements((int) boards.getTotalElements())
                .isFirst(boards.isFirst())
                .isLast(boards.isLast())
                .build();

    }

    public BoardDetailResponse toBoardDetailResponse(Board board, List<String> boardFiles) {
        return BoardDetailResponse.builder()
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .writer(board.getWriter().getNickname()+"/"+board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .part(board.getWriter().getRecentPart())
                .semester(board.getWriter().getRecentSemester())
                .title(board.getTitle())
                .content(board.getContent())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .boardFiles(boardFiles)
                .createdAt(board.getCreatedAt())
                .build();
    }
}
