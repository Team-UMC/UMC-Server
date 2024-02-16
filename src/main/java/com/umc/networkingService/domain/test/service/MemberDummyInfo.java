package com.umc.networkingService.domain.test.service;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.umc.networkingService.global.common.enums.Part.*;
import static com.umc.networkingService.global.common.enums.Semester.*;

@Getter
@RequiredArgsConstructor
public enum MemberDummyInfo {
    MEMBER1("챌린저","김수민","루시",List.of(FIFTH),List.of(SPRING)),
    MEMBER2("챌린저","오정현","델로", List.of(FIFTH),List.of(DESIGN)),
    MEMBER3("챌린저","양유진","더기",List.of(FIFTH),List.of(DESIGN)),
    MEMBER4("챌린저","정진혁","눈꽃", List.of(FIFTH),List.of(WEB)),
    MEMBER5("챌린저","이원영","리오", List.of(FIFTH),List.of(WEB)),
    MEMBER6("챌린저","김민지","율리", List.of(FIFTH),List.of(WEB)),
    MEMBER7("챌린저","최재혁","체이서", List.of(FIFTH),List.of(WEB)),
    MEMBER8("챌린저","김보민","밈보", List.of(FIFTH),List.of(SPRING)),
    MEMBER9("챌린저","박재우","다재",List.of(FIFTH),List.of(SPRING)),
    MEMBER10("챌린저","김오비","비오",List.of(FOURTH),List.of(ANDROID)),
    CampusStaff("iOS 파트장","이경수","리버", List.of(FIFTH,FOURTH),List.of(SPRING,IOS)),
    BranchStaff("회장","김준석", "벡스",List.of(FIFTH,THIRD),List.of(SPRING,ANDROID)),
    CenterStaff("운영국장","김루시", "시루",List.of(FIFTH),List.of(SPRING)),

    ;

    private final String position;
    private final String name;
    private final String nickname;
    private final List<Semester> semesters;
    private final List<Part> parts;
}


