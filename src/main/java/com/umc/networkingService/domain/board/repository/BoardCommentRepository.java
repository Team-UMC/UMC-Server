package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardCommentRepository extends JpaRepository<BoardComment, UUID> {
}
