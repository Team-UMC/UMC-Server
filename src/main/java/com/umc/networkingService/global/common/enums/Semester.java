package com.umc.networkingService.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Semester {
    FIRST("1기", "RED"),
    SECOND("2기", "ORANGE"),
    THIRD("3기", "YELLOW"),
    FOURTH("4기", "GREEN"),
    FIFTH("5기", "BLUE");

    private final String name;

    private final String color;
}
