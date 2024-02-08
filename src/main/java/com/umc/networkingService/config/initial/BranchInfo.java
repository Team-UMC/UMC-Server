package com.umc.networkingService.config.initial;

import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BranchErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static com.umc.networkingService.config.initial.UniversityInfo.*;

@Getter
@RequiredArgsConstructor
public enum BranchInfo {

    // 4기
    NEO("NEO(네오)", "NEO 지부입니다.", Semester.FOURTH, List.of(KWANGWOON_UNIV, DONGDUK_WOMENS_UNIV, SEOUL_WOMENS_UNIV, SOONGSIL_UNIV, HANKUK_FOREIGN_STUDIES_UNIV)),
    DEST("DEST(데스트)", "DEST 지부입니다.", Semester.FOURTH, List.of(DUKSUNG_WOMENS_UNIV, DONGGUK_UNIV, SANGMYUNG_UNIV, SUNGSHIN_WOMENS_UNIV)),
    PEACOCK("PEACOCK(피콕)", "PEACOCK 지부입니다.", Semester.FOURTH, List.of(MYONGJI_UNIV, SOOKMYUNG_WOMENS_UNIV, EWHA_WOMANS_UNIV, KOREA_AEROSPACE_UNIV, HONGIK_UNIV)),
    BLUE("BLUE(블루)", "BLUE 지부입니다.", Semester.FOURTH, List.of(CATHOLIC_UNIV, CHUNGANG_UNIV, INHA_UNIV, KOREA_TECHNOLOGY_AND_EDUCATION_UNIV, HANYANG_ERICA_UNIV)),
    GOGIGUI("고기구이", "고기구이 지부입니다.", Semester.FOURTH, List.of(GACHON_UNIV, KYONGGI_UNIV, KYUNGHEE_UNIV, AJOU_UNIV)),
    LIFO("LIFO(리포)", "LIFO 지부입니다.", Semester.FOURTH, List.of(PUKYONG_UNIV, GYEONGSANG_NATIONAL_UNIV, ULSAN_UNIV)),

    // 5기
    SQUARE("SQUARE(스퀘어)", "SQAURE 지부입니다.", Semester.FIFTH, List.of(HONGIK_UNIV, DONGDUK_WOMENS_UNIV, HANSUNG_UNIV, KOREA_AEROSPACE_UNIV)),
    W("W(더블유)", "W 지부입니다.", Semester.FIFTH, List.of(DUKSUNG_WOMENS_UNIV, SEOKYEONG_UNIV, SEOUL_WOMENS_UNIV, KWANGWOON_UNIV, HANKUK_FOREIGN_STUDIES_UNIV)),
    GACI("GACI(가치)", "GACI 지부입니다.", Semester.FIFTH, List.of(GACHON_UNIV, CATHOLIC_UNIV, AJOU_UNIV, INHA_UNIV)),
    HEDGES("HEDGES(헤지스)", "HEDGES 지부입니다.", Semester.FIFTH, List.of(EWHA_WOMANS_UNIV, DONGGUK_UNIV, KYONGGI_UNIV, HANYANG_UNIV, SOOKMYUNG_WOMENS_UNIV)),
    KSSS("KSSS(키세스)", "KSSS 지부입니다.", Semester.FIFTH, List.of(SUNGSHIN_WOMENS_UNIV, KYUNGHEE_UNIV, SOONGSIL_UNIV, SANGMYUNG_UNIV)),
    CHEMI("CHEMI(케미)", "CHEMI 지부입니다.", Semester.FIFTH, List.of(HANYANG_ERICA_UNIV, KOREA_TECHNOLOGY_AND_EDUCATION_UNIV, CHUNGANG_UNIV, MYONGJI_UNIV)),
    NEPTUNE("NEPTUNE(넵튠)", "NEPTUNE 지부입니다.", Semester.FIFTH, List.of(GYEONGSANG_NATIONAL_UNIV, PUKYONG_UNIV, ULSAN_UNIV)),
    ;
    private final String name;
    private final String description;
    private final Semester semester;
    private final List<UniversityInfo> universities;

    public static BranchInfo getBranchInfo(String name) {
        return Arrays.stream(BranchInfo.values())
                .filter(branchInfo -> branchInfo.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RestApiException(BranchErrorCode.BRANCH_NOT_FOUND));
    }
}
