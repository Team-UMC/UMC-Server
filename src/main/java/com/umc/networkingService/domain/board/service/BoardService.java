package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardSearchPagingResponse;
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
    BoardIdResponse createBoard(Member member, BoardCreateRequest request, List<MultipartFile> files);

    BoardIdResponse updateBoard(Member member, UUID boardId, BoardUpdateRequest request, List<MultipartFile> files);

    BoardIdResponse deleteBoard(Member member, UUID boardId);

    BoardPagingResponse showBoards(Member member, HostType host, BoardType board, Pageable pageable);

    BoardDetailResponse showBoardDetail(Member member, UUID boardId);

    BoardSearchPagingResponse searchBoard(Member member, String keyword, Pageable pageable);

    BoardIdResponse toggleBoardLike(Member member, UUID boardId);
    BoardSearchPagingResponse showMemberBoards(Member member, String keyword, Pageable pageable);
    BoardSearchPagingResponse showMemberBoardHearts(Member member, String keyword, Pageable pageable);
    BoardSearchPagingResponse showBoardsByMemberComments(Member member, String keyword, Pageable pageable);

}
