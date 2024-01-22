package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardFileRepository extends JpaRepository<BoardFile, UUID> {
    List<BoardFile> findAllByBoard(Board board);
}
