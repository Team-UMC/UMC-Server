package com.umc.networkingService.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Semester {
    FIRST("1기", "RED",false),
    SECOND("2기", "ORANGE",false),
    THIRD("3기", "YELLOW",false),
    FOURTH("4기", "GREEN",false),
    FIFTH("5기", "BLUE",true);

    private final String name;
    private final String color;

    private final boolean isActive;


    //현재 기수 찾기
    public static Semester findActiveSemester() {
        return Arrays.stream(Semester.values())
                .filter(Semester::isActive)
                .findFirst()
                .orElse(FIFTH);
    }
}
