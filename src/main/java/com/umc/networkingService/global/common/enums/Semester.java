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

    // 가장 마지막 기수를 반환하는 함수
    public static Semester getLastSemester() {
        Semester[] semesters = Semester.values();
        return semesters[semesters.length - 1];
    }
}
