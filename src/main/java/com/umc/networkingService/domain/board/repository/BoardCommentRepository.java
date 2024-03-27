package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.BoardComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BoardCommentRepository extends JpaRepository<BoardComment, UUID>, BoardRepositoryCustom {
    Optional<BoardComment> findById(UUID commentId);

    boolean existsByParentComment(BoardComment boardComment);
}