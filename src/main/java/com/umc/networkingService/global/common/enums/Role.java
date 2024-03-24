package com.umc.networkingService.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자", 0),
    TOTAL_STAFF("총괄운영진", 1),
    CENTER_STAFF("중앙운영진", 2),
    BRANCH_STAFF("지부운영진", 3),
    CAMPUS_STAFF("학교운영진", 4),
    STAFF("운영진", 4),
    MEMBER("챌린저", 5);

    private final String toKorean;
    private final int priority;

}
