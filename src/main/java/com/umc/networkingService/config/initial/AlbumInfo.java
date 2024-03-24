package com.umc.networkingService.config.initial;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum AlbumInfo {

    // 4기
    FOURTH_INHA_MT("222222", Semester.FOURTH, "인하대학교 4기 MT", "OB와 YB가 함께하는 인하대학교 4기 MT를 성공적으로 마무리했습니다.",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/MT.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/MT+(2).jpg")),
    FOURTH_DEMO_DAY("222222", Semester.FOURTH, "UMC 4기 데모데이 행사", "UMC 4기 데모데이 행사 3일간 성공적으로 마무리했습니다.",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/%EB%8D%B0%EB%AA%A8%EB%8D%B0%EC%9D%B41.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/%EB%8D%B0%EB%AA%A8%EB%8D%B0%EC%9D%B42.jpg")),

    // 5기
    FIFTH_OT("333333", Semester.FIFTH, "UMC 5기 OT", "UMC 5기 연합 및 교내 OT를 진행하였습니다.",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/OT1.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/OT2.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/OT3.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/OT4.jpg")),
    FIFTH_NETWORKING("333333", Semester.FIFTH, "UMC 5기 GACI 지부 네트워킹 데이", "UMC 5기 GACI 지부 네트워킹 데이 행사를 진행하였습니다.",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/NETWORKING1.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/NETWORKING2.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/NETWORKING3.jpg")),
    FIFTH_INHA_MT("333333", Semester.FIFTH, "인하대학교 5기 MT", "OB와 YB가 함께하는 인하대학교 5기 MT를 진행하였습니다.",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/MT1.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/MT2.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/MT3.jpg")),
    FIFTH_GRDUATION("333333", Semester.FIFTH, "인하대학교 UMC 졸업식 행사", "인하대학교 UMC 챌린저 졸업자를 축하하는 행사를 진행했습니다.",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/G1.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/G2.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/G3.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/G4.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/G5.jpg")),
    FIFTH_DEMO_DAY("333333", Semester.FIFTH, "UMC 5기 데모데이 행사", "UMC 5기 데모데이 행사를 3일간 성공적으로 마무리했고, UMC 네트워킹 데이 서비스 팀이 1등했습니다!!~~",
            List.of("https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/DEMO.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/DEMO1.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/DEMO2.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/DEMO3.jpg",
                    "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/album/inha/DEMO4.jpg")),

    ;

    private final String clientId;
    private final Semester semester;
    private final String title;
    private final String content;
    private final List<String> images;
}
