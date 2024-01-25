package com.umc.networkingService.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRelation {
    MINE("본인"),
    FRIEND("친구"),
    OTHERS("그 외");

    private final String description;
}
