package com.umc.networkingService.domain.board.mapper;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardMapper {
    public static Board toEntity(Member member, String title, String content, HostType hostType, BoardType boardType,
                                 List<Semester> semesterPermission) {
        return Board.builder()
                .writer(member)
                .title(title)
                .content(content)
                .hostType(hostType)
                .boardType(boardType)
                .semesterPermission(semesterPermission)
                .build();
    }
}
