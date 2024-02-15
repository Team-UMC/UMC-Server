package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.mascot.entity.MascotType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum MascotInfo {
    U_STEP1(1, 10, MascotType.UU, List.of("나 머리가 간지러워", "반가워!!"), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/U_LV1.png"),
    U_STEP2(11, 20, MascotType.UU, List.of("나는 씩씩한 입을 갖고 싶어", "곧 다음 단계야"), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/U_LV2.png"),
    U_STEP3(21, 30, MascotType.UU, List.of("나 멋있지?", "조금만 더 노력하면 마지막 단계야"), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/U_LV3.png"),
    U_STEP4(31, 40, MascotType.UU, List.of("우리 학교 짱짱", "날 키워줘서 고마워"), ""),
    M_STEP1(1, 10, MascotType.MM, List.of("저는 왜 귀가 없을까요", "안녕하세요"), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/M_LV1.png"),
    M_STEP2(11, 20, MascotType.MM, List.of("저 볼이 간지러워요", "조금만 더 노력하면 다음 단계에요"), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/M_LV2.png"),
    M_STEP3(21, 30, MascotType.MM, List.of("저는 고양이이에요 야옹~", "곧 마지막 단계에요"), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/M_LV3.png"),
    M_STEP4(31, 40, MascotType.MM, List.of("우리 학교 짱이에요", "절 키워줘서 고마워요"), ""),
    C_STEP1(1, 10, MascotType.CC, List.of("...", "안녕..."), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/C_LV1.png"),
    C_STEP2(11, 20, MascotType.CC, List.of("머리 자랐다...", "조금만 더 노력해줘..."), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/C_LV2.png"),
    C_STEP3(21, 30, MascotType.CC, List.of("입 생겼다...", "곧 마지막..."), "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/C_LV3.png"),
    C_STEP4(31, 40, MascotType.CC, List.of("우리 학교... 짱...", "키워줘서... 고마워..."), ""),
    ;
    private final int startLevel;
    private final int endLevel;
    private final MascotType mascotType;
    private final List<String> dialogue;
    private final String imageUrl;
}
