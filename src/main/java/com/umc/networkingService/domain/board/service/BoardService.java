package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.response.BoardResponse.*;

public interface BoardService extends EntityLoader<Board, UUID> {
    BoardId createBoard(Member member, BoardRequest.BoardCreateRequest request, List<MultipartFile> files);

    BoardId updateBoard(Member member, UUID boardId, BoardRequest.BoardUpdateRequest request, List<MultipartFile> files);

    BoardId deleteBoard(Member member, UUID boardId);

    PinnedNotices showPinnedNotices(Member member, HostType hostType);

    BoardPageInfos<BoardPageElement> showBoards(Member member, HostType host, BoardType board, Pageable pageable);

    BoardDetail showBoardDetail(Member member, UUID boardId);

    BoardPageInfos<BoardSearchPageElement> searchBoard(Member member, String keyword, Pageable pageable);

    BoardPageInfos<MyBoardPageElement> showBoardsByMember(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

    BoardPageInfos<MyBoardPageElement> showBoardsByMemberHeart(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

    BoardId toggleBoardLike(Member member, UUID boardId);
    Boolean existsByBoardTypeAndHostType(BoardType boardType, HostType hostType);
    boolean checkWriter(Member writer, Member member);
    boolean checkHighStaff(Member writer, Member member);


}
