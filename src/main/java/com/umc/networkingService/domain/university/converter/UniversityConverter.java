package com.umc.networkingService.domain.university.converter;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;

import java.util.List;
import java.util.stream.Collectors;

public class UniversityConverter {

    // 대학교 리스트 조회
    public static List<UniversityResponse.JoinUniversity> toJoinUniversityList(
            List<University> universityList
    ) {
        return universityList.stream()
                .map(university -> UniversityResponse.JoinUniversity.builder()
                        .universityId(university.getId())
                        .universityName(university.getName())
                        .build())
                .collect(Collectors.toList());
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

    // 학교 랭킹 조회
    public static List<UniversityResponse.JoinUniversityRank> toJoinUniversityRankList(
            List<University> universityRankList
    ) {
        return universityRankList.stream()
                .map(university -> UniversityResponse.JoinUniversityRank.builder()
                        .universityName(university.getName())
                        .universityLogo(university.getUniversityLogo())
                        .universityPoint(university.getTotalPoint())
                        .universityRank(universityRankList.indexOf(university)+1)
                        .build())
                .collect(Collectors.toList());
    }

    // 학교 기여도 랭킹 조회
    public static List<UniversityResponse.JoinContributionRank> toJoinContributionRankList(
            List<Member> contributionRankList
    ){
        return contributionRankList.stream()
                .map(member -> UniversityResponse.JoinContributionRank.builder()
                        .nickname(member.getNickname())
                        .name(member.getName())
                        .profileImage(member.getProfileImage())
                        .usedPoint(member.getContributionPoint())
                        .rank(contributionRankList.indexOf(member)+1)
                        .build())
                .collect(Collectors.toList());
    }

    // 학교 마스코트 조회
    public static UniversityResponse.joinUniversityMascot toJoinUniversityMascot(
            University university, Mascot mascot
    ){
        return UniversityResponse.joinUniversityMascot.builder()
                .mascotDialog(mascot.getDialogue())
                .mascotImage(mascot.getImage())
                .level(university.getCurrentLevel())
                .point(university.getTotalPoint())
                .build();
    }


}

