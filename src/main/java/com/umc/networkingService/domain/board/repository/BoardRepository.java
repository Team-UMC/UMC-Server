package com.umc.networkingService.domain.board.repository;

import com.umc.networkingService.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  BoardRepository extends JpaRepository<Board,Long> {

}
