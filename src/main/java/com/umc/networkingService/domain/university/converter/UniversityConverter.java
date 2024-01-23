package com.umc.networkingService.domain.university.converter;

import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;

import java.util.List;
import java.util.stream.Collectors;

public class UniversityConverter {

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

}

