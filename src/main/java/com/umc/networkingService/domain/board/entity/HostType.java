package com.umc.networkingService.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HostType {
    CENTER("연합",3),
    BRANCH("지부",4),
    CAMPUS("학교",5);

    private final String toKorean;
    private final int priority;
}
