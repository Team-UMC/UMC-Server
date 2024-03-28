package com.umc.networkingService.domain.board.entity;

import com.umc.networkingService.global.common.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HostType {
    CENTER("연합", 3),
    BRANCH("지부", 4),
    CAMPUS("학교", 5);

    private final String toKorean;
    private final int priority;

    //허용 가능한 최상위 hostType
    public static HostType getPermmissionHostType(Role role) {
        if (role.ordinal() <= Role.CENTER_STAFF.ordinal())
            return CENTER;
        else if (role.ordinal() == Role.BRANCH_STAFF.ordinal())
            return BRANCH;
        else
            return CAMPUS;
    }
}
