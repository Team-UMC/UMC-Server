package com.umc.networkingService.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    PUDDING("은하수를 살짝 얹은 커스터드 푸딩", 1L, null),
    DOUGHNUT("은하스프링클 도넛", 5L, null),
    ICE_CREAM("태양이 물든 선샤인 샤베트 아이스크림", 10L, null),
    ROLL_CAKE("찬란한 별들이 쑥쑥 박힌 우주맛 롤케이크", 30L, null),
    ;

    private final String description;

    private final Long point;

    private final String image;
}
