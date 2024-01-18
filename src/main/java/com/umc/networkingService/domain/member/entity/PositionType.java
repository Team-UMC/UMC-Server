package com.umc.networkingService.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    CENTER("중앙"),
    CAMPUS("교내");

    private final String Korean;
}
