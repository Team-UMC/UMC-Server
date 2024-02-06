package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BoardSearchPageResponse extends BoardPageResponse {
    private HostType hostType;
    private BoardType boardType;
}
