package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BoardService extends EntityLoader<Board, UUID> {
    BoardResponse.BoardId createBoard(Member member, BoardRequest.BoardCreateRequest request, List<MultipartFile> files);

    BoardResponse.BoardId updateBoard(Member member, UUID boardId, BoardRequest.BoardUpdateRequest request, List<MultipartFile> files);

    BoardResponse.BoardId deleteBoard(Member member, UUID boardId);
    BoardResponse.PinnedNotices showPinnedNotices(Member member);

    BoardResponse.BoardPageInfos showBoards(Member member, HostType host, BoardType board, Pageable pageable);
    BoardResponse.BoardDetail showBoardDetail(Member member, UUID boardId);

    BoardResponse.BoardSearchPageInfos searchBoard(Member member, String keyword, Pageable pageable);

    BoardResponse.BoardId toggleBoardLike(Member member, UUID boardId);

    MyBoardResponse.MyBoardPageInfos showBoardsByMemberForApp(Member member, String keyword, Pageable pageable);
    MyBoardResponse.MyBoardPageInfos showBoardsByMemberForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);
    MyBoardResponse.MyBoardPageInfos showBoardsByMemberHeartForApp(Member member, String keyword, Pageable pageable);
    MyBoardResponse.MyBoardPageInfos showBoardsByMemberHeartForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

}
