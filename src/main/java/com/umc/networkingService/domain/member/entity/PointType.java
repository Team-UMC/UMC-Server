package com.umc.networkingService.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    PUDDING("은하수를 살짝 얹은 커스터드 푸딩", 5L, null),
    DOUGHNUT("은하스프링클 도넛", 10L, null);

    private final String title;

    private final Long point;

    private final String image;
}
