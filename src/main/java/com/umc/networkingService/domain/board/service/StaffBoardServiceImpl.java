package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffBoardServiceImpl implements StaffBoardService {
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @Override
    public BoardPagingResponse showAllCampusNotices(Member member, String keyword, Pageable pageable) {

        return boardMapper.toBoardPagingResponse(boardRepository.findAllCampusNotices(member, keyword, pageable));
    }

    @Override
    @Transactional
    public BoardIdResponse toggleNoticePin(Member member, UUID boardId, boolean isPinned) {

        Board board = boardService.loadEntity(boardId);
        board.setIsFixed(isPinned);

        return new BoardIdResponse(board.getId());
    }

}
