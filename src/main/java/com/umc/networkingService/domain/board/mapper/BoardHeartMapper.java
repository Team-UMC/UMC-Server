package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardHeart;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardHeartMapper {

    public BoardHeart toBoardHeartEntity(Board board, Member member) {
        return BoardHeart.builder()
                .board(board)
                .member(member)
                .isChecked(false)
                .build();
    }
}
