package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID>, BoardRepositoryCustom {

    Optional<Board> findById(UUID boardId);

    boolean existsByBoardTypeAndHostType(BoardType boardType, HostType hostType);

}
