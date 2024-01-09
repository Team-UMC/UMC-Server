package com.umc.networkingService.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자", null),
    STAFF("운영진", null),
    CENTERSTAFF("중앙운영진", null),
    BRANCHSTAFF("지부운영진", null),
    CAMPUSSTAFF("학교운영진", null),
    MEMBER("챌린저", null);

    private final String toKorean;

    private final Long priority;
}
