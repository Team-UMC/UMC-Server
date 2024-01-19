package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface  BoardRepository extends JpaRepository<Board,UUID> {
    @Query(value = "select b from Board b where b.id = :boardId and b.deletedAt is null")
    Optional<Board> findById(@Param("boardId") UUID boardId);
}
