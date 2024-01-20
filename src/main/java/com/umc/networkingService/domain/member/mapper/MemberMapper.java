package com.umc.networkingService.domain.member.mapper;

import com.umc.networkingService.domain.member.dto.response.MemberInquiryHomeInfoResponse;
import com.umc.networkingService.domain.member.dto.response.MemberInquiryPointsResponse;
import com.umc.networkingService.domain.member.dto.response.MemberInquiryProfileResponse;
import com.umc.networkingService.domain.member.entity.*;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MemberMapper {

    public MemberPosition toMemberPosition(Member member, PositionType type, String position) {
        return MemberPosition.builder()
                .name(position)
                .member(member)
                .type(type)
                .build();
    }

    public MemberInquiryProfileResponse toInquiryProfileResponse(Member member, MemberRelation owner) {

        String universityName = Optional.ofNullable(member.getUniversity())
                .map(University::getName)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER_UNIVERSITY));

        return MemberInquiryProfileResponse.builder()
                .memberId(member.getId())
                .profileImage(member.getProfileImage())
                .universityName(universityName)
                .name(member.getName())
                .nickname(member.getNickname())
                .parts(member.getParts())
                .semesters(member.getSemesters())
                .statusMessage(member.getStatusMessage())
                .owner(owner)
                .build();
    }

    public MemberInquiryHomeInfoResponse toInquiryHomeInfoResponse(Member member, int rank) {
        return MemberInquiryHomeInfoResponse.builder()
                .profileImage(member.getProfileImage())
                .nickname(member.getNickname())
                .contributionPoint(member.getContributionPoint())
                .contributionRank(rank)
                .build();
    }

    public MemberInquiryPointsResponse.UsedHistory toUsedHistory(PointType pointType) {
        return MemberInquiryPointsResponse.UsedHistory.builder()
                .pointImage(pointType.getImage())
                .point(pointType.getPoint())
                .description(pointType.getDescription())
                .build();
    }

    public MemberInquiryPointsResponse toInquiryPointsResponse(Long point,
                                                               List<MemberInquiryPointsResponse.UsedHistory> usedHistories) {
        return MemberInquiryPointsResponse.builder()
                .remainPoint(point)
                .usedHistories(usedHistories)
                .build();
    }
}
