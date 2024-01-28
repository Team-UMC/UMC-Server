package com.umc.networkingService.config.initial;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UniversityInfo {
    GACHON_UNIV("가천대학교", "", ""),
    CATHOLIC_UNIV("가톨릭대학교", "", ""),
    KYONGGI_UNIV("경기대학교", "", ""),
    GYEONGSANG_NATIONAL_UNIV("경상국립대학교", "", ""),
    KYUNGHEE_UNIV("경희대학교", "", ""),
    KWANGWOON_UNIV("광운대학교", "", ""),
    DUKSUNG_WOMENS_UNIV("덕성여자대학교", "", ""),
    DONGGUK_UNIV("동국대학교", "", ""),
    DONGDUK_WOMENS_UNIV("동덕여자대학교", "", ""),
    MYONGJI_UNIV("명지대학교", "", ""),
    PUKYONG_UNIV("부경대학교", "", ""),
    SANGMYUNG_UNIV("상명대학교", "", ""),
    SEOKYEONG_UNIV("서경대학교", "", ""),
    SEOUL_WOMENS_UNIV("서울여자대학교", "", ""),
    SUNGSHIN_WOMENS_UNIV("성신여자대학", "", ""),
    SOOKMYUNG_WOMENS_UNIV("숙명여자대학교", "", ""),
    SOONGSIL_UNIV("숭실대학교", "", ""),
    AJOU_UNIV("아주대학교", "", ""),
    ULSAN_UNIV("울산대학교", "", ""),
    EWHA_WOMANS_UNIV("이화여자대학교", "", ""),
    INHA_UNIV("인하대학교", "", ""),
    CHUNGANG_UNIV("중앙대학교", "", ""),
    KOREA_TECHNOLOGY_AND_EDUCATION_UNIV("한국공과대학교", "", ""),
    HANKUK_FOREIGN_STUDIES_UNIV("한국외국어대학교", "", ""),
    KOREA_AEROSPACE_UNIV("한국항공대학교", "", ""),
    HANSUNG_UNIV("한성학교", "", ""),
    HANYANG_UNIV("한양대학교", "", ""),
    HANYANG_ERICA_UNIV("한양대학교 ERICA", "", ""),
    HONGIK_UNIV("홍익대학교", "", "")
    ;

    private final String name;
    private final String universityLogo;
    private final String semesterLogo;
}
