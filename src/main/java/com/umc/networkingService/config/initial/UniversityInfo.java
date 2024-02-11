package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.mascot.entity.MascotType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UniversityInfo {
    GACHON_UNIV("가천대학교", "", "", MascotType.UU),
    CATHOLIC_UNIV("가톨릭대학교", "", "", MascotType.MM),
    KYONGGI_UNIV("경기대학교", "", "", MascotType.CC),
    GYEONGSANG_NATIONAL_UNIV("경상국립대학교", "", "", MascotType.UU),
    KYUNGHEE_UNIV("경희대학교", "", "", MascotType.MM),
    KWANGWOON_UNIV("광운대학교", "", "", MascotType.CC),
    DUKSUNG_WOMENS_UNIV("덕성여자대학교", "", "", MascotType.UU),
    DONGGUK_UNIV("동국대학교", "", "", MascotType.MM),
    DONGDUK_WOMENS_UNIV("동덕여자대학교", "", "", MascotType.CC),
    MYONGJI_UNIV("명지대학교", "", "", MascotType.UU),
    PUKYONG_UNIV("부경대학교", "", "", MascotType.MM),
    SANGMYUNG_UNIV("상명대학교", "", "", MascotType.CC),
    SEOKYEONG_UNIV("서경대학교", "", "", MascotType.UU),
    SEOUL_WOMENS_UNIV("서울여자대학교", "", "", MascotType.MM),
    SUNGSHIN_WOMENS_UNIV("성신여자대학", "", "", MascotType.CC),
    SOOKMYUNG_WOMENS_UNIV("숙명여자대학교", "", "", MascotType.UU),
    SOONGSIL_UNIV("숭실대학교", "", "", MascotType.MM),
    AJOU_UNIV("아주대학교", "", "", MascotType.CC),
    ULSAN_UNIV("울산대학교", "", "", MascotType.UU),
    EWHA_WOMANS_UNIV("이화여자대학교", "", "", MascotType.MM),
    INHA_UNIV("인하대학교", "", "", MascotType.CC),
    CHUNGANG_UNIV("중앙대학교", "", "", MascotType.UU),
    KOREA_TECHNOLOGY_AND_EDUCATION_UNIV("한국공과대학교", "", "", MascotType.MM),
    HANKUK_FOREIGN_STUDIES_UNIV("한국외국어대학교", "", "", MascotType.CC),
    KOREA_AEROSPACE_UNIV("한국항공대학교", "", "", MascotType.UU),
    HANSUNG_UNIV("한성학교", "", "", MascotType.MM),
    HANYANG_UNIV("한양대학교", "", "", MascotType.CC),
    HANYANG_ERICA_UNIV("한양대학교 ERICA", "", "", MascotType.UU),
    HONGIK_UNIV("홍익대학교", "", "", MascotType.MM)
    ;

    private final String name;
    private final String universityLogo;
    private final String semesterLogo;
    private final MascotType mascotType;
}
