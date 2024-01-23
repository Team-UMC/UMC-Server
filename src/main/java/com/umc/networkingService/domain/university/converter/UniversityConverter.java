package com.umc.networkingService.domain.university.converter;

import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;

import java.util.List;
import java.util.stream.Collectors;

public class UniversityConverter {

    // 대학교 리스트 조회
    public static UniversityResponse.joinUniversityList toJoinUniversityList(
            List<University> universityList
    ) {
        return UniversityResponse.joinUniversityList.builder()
                .universityList(
                        universityList.stream().map(
                                university -> UniversityResponse.UniversityList.builder()
                                        .universityId(university.getId())
                                        .universityName(university.getName())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    // 학교 별 세부 정보 조회
    public static UniversityResponse.joinUniversityDetail toJoinUniversityDetail(
            University university
    ) {
        return UniversityResponse.joinUniversityDetail.builder()
                .universityId(university.getId())
                .universityName(university.getName())
                .universityLogo(university.getUniversityLogo())
                .universityPoint(university.getTotalPoint())
                .build();
    }

}

