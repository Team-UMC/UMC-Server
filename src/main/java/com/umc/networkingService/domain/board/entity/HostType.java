package com.umc.networkingService.domain.board.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HostType {
    CENTER("연합"),
    BRANCH("지부"),
    CAMPUS("학교");

    private final String Korean;
}
