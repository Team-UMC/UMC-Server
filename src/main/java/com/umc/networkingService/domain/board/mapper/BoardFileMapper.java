package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import org.springframework.stereotype.Component;

@Component
public class BoardFileMapper {

    public static BoardFile toEntity(Board board, String url) {
        return BoardFile.builder()
                .board(board)
                .url(url).build();
    }
}
