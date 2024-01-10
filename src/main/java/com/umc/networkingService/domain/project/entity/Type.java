package com.umc.networkingService.domain.project.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    AOS("안드로이드"),
    IOS("아이오에스"),
    WEB("웹");

    private final String toKorean;

}
