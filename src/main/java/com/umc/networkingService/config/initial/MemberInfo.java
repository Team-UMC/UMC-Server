package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberInfo {

    // 4기 회장
    BORA("222222", "인하대학교", "보라", "유지수", SocialType.KAKAO, Role.CAMPUS_STAFF, "회장", Semester.FOURTH, Part.SPRING),

    // 5기 회장
    BEX("333333", "인하대학교", "벡스", "김준석", SocialType.KAKAO, Role.CAMPUS_STAFF, "회장", Semester.FIFTH, Part.SPRING),
    ;

    private final String clientId;
    private final String university;
    private final String nickname;
    private final String name;
    private final SocialType type;
    private final Role role;
    private final String position;
    private final Semester semester;
    private final Part part;


}
