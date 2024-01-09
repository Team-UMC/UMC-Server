package com.umc.networkingService.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
    LEADER("회장"),
    SECONDLEADER("부회장"),
    MANAGER("총무"),
    PMLEADER("피엠파트장"),
    DESIGNLEADER("디자인파트장"),
    SPRINGLEADER("스프링파트장"),
    NODELEADER("노드파트장"),
    WEBLEADER("웹파트장"),
    IOSLEADER("아이오에스파트장"),
    ANDROIDLEADER("안드로이드파트장");

    private final String toKorean;
}
