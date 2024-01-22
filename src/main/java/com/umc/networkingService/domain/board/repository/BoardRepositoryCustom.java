package com.umc.networkingService.domain.board.repository;


import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findAllBoards(Member member, HostType hostType, BoardType boardType, Pageable pageable);

    Page<Board> findKeywordBoards(Member member, String keyword, Pageable pageable);
}
