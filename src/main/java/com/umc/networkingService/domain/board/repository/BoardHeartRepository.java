package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardHeart;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardHeartRepository extends JpaRepository<BoardHeart, UUID> {
    Optional<BoardHeart> findByMemberAndBoard(Member member, Board board);

}
