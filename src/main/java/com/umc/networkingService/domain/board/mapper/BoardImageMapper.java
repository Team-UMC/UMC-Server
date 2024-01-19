package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardImage;
import org.springframework.stereotype.Component;

@Component
public class BoardImageMapper {

    public static BoardImage toEntity(Board board, String url) {
        return BoardImage.builder()
                .board(board)
                .url(url).build();
    }
}
