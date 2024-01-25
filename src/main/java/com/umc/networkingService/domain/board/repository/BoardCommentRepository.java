package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BoardCommentRepository extends JpaRepository<BoardComment, UUID>, BoardRepositoryCustom {
    @Query(value = "select b from BoardComment b where b.id = :commentId and b.deletedAt is null")
    Optional<BoardComment> findById(@Param("commentId") UUID commentId);

}