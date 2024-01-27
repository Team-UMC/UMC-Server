package com.umc.networkingService.domain.board.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BoardIdResponse {
    private UUID boardId;
}
