package com.umc.networkingService.domain.board.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardType {
    NOTICE("공지"),
    FREE("자유"),
    WORKBOOK("워크북"),
    QUESTION("질문"),
    OB("이전 기수");

    private final String toKorean;
}
