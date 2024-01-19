package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardImageRepository extends JpaRepository<BoardImage, UUID> {

}
