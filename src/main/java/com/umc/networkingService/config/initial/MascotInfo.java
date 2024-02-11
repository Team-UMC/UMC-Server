package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.mascot.entity.MascotType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MascotInfo {
    U_STEP1(1, 10, MascotType.UU, "나 머리가 간지러워", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/U_LV1.png"),
    U_STEP2(11, 20, MascotType.UU, "나 말하고 싶어", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/U_LV2.png"),
    U_STEP3(21, 30, MascotType.UU, "입 생겼다~", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/U_LV3.png"),
    U_STEP4(31, 40, MascotType.UU, "", ""),
    M_STEP1(1, 10, MascotType.MM, "나는 왜 귀가 없을까", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/M_LV1.png"),
    M_STEP2(11, 20, MascotType.MM, "나 볼이 간지러워", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/M_LV2.png"),
    M_STEP3(21, 30, MascotType.MM, "나는 고양이다~", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/M_LV3.png"),
    M_STEP4(31, 40, MascotType.MM, "", ""),
    C_STEP1(1, 10, MascotType.CC, "...", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/C_LV1.png"),
    C_STEP2(11, 20, MascotType.CC, "머리 자랐다...", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/C_LV2.png"),
    C_STEP3(21, 30, MascotType.CC, "입 생겼다...", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/mascot/C_LV3.png"),
    C_STEP4(31, 40, MascotType.CC, "", ""),
    ;
    private final int startLevel;
    private final int endLevel;
    private final MascotType mascotType;
    private final String dialogue;
    private final String imageUrl;
}
