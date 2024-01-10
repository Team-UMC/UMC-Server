package com.umc.networkingService.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Part {
    PM("피엠", "RED"),
    DESIGN("디자인", "ORANGE"),
    SPRING("스프링", "YELLOW"),
    NODE("노드", "GREEN"),
    IOS("아이오에스", "BLUE"),
    ANDROID("안드로이드", "NAVY"),
    WEB("웹", "PURPLE"),
    ETC("기타", "BLACK");

    private final String Korean;

    private final String color;
}
