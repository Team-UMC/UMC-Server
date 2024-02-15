package com.umc.networkingService.domain.mascot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MascotType {
    UU("마스코트 종류 U"),
    MM("마스코트 종류 M"),
    CC("마스코트 종류 C");

    private final String description;
}
