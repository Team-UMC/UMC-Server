package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffBoardServiceImpl implements StaffBoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

   /* public BoardPagingResponse showAllNotices(Member member, Pageable pageable) {
        return boardMapper.toBoardPagingResponse(boardRepository.findAllNotices(member, pageable));
    }*/

}
