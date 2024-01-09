package com.umc.networkingService.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Part {
    PM("피엠", "RED", null),
    DESIGN("디자인", "ORANGE", null),
    SPRING("스프링", "YELLOW", null),
    NODE("노드", "GREEN", null),
    IOS("아이오에스", "BLUE", null),
    ANDROID("안드로이드", "NAVY", null),
    WEB("웹", "PURPLE", null);

    private final String Korean;

    private final String color;

    private final String partImage;
}
