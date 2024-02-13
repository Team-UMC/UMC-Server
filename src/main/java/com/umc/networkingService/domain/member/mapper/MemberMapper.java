package com.umc.networkingService.domain.member.mapper;

import com.umc.networkingService.config.security.jwt.TokenInfo;
import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.*;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.MemberErrorCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

;

@Component
public class MemberMapper {
    public Member toMember(final String clientId, SocialType socialType){
        return Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .role(Role.MEMBER)
                .build();
    }

    public MemberLoginResponse toLoginMember(final Member member, TokenInfo tokenInfo, boolean isServiceMember) {
        return MemberLoginResponse.builder()
                .memberId(member.getId())
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .isServiceMember(isServiceMember)
                .build();
    }
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
                .orElseThrow(() -> new RestApiException(MemberErrorCode.EMPTY_MEMBER_UNIVERSITY));

        return MemberInquiryProfileResponse.builder()
                .memberId(member.getId())
                .profileImage(member.getProfileImage())
                .universityName(universityName)
                .name(member.getName())
                .nickname(member.getNickname())
                .semesterParts(toSemesterPartInfos(member.getSemesterParts()))
                .statusMessage(member.getStatusMessage())
                .owner(owner)
                .build();
    }

    public MemberInquiryInfoWithPointResponse toInquiryHomeInfoResponse(Member member, int rank) {
        return MemberInquiryInfoWithPointResponse.builder()
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

    public MemberSearchInfosResponse.MemberInfo toSearchMembersResponse(
            Member member, List<String> campusPositions, List<String> centerPositions) {

        return  MemberSearchInfosResponse.MemberInfo.builder()
                .memberId(member.getId())
                .universityName(member.getUniversity().getName())
                .campusPositions(campusPositions)
                .centerPositions(centerPositions)
                .semesterParts(toSemesterPartInfos(member.getSemesterParts()))
                .build();

    }

    public SemesterPart toSemesterPart(Member member, SemesterPartInfo semesterPartInfo) {
        return SemesterPart.builder()
                .member(member)
                .part(semesterPartInfo.getPart())
                .semester(semesterPartInfo.getSemester())
                .build();
    }

    public List<SemesterPartInfo> toSemesterPartInfos(List<SemesterPart> semesterParts) {
        return semesterParts.stream()
                .map(this::toSemesterPartInfo)
                .toList();
    }

    private SemesterPartInfo toSemesterPartInfo(SemesterPart semesterPart) {
        return new SemesterPartInfo(semesterPart.getPart(), semesterPart.getSemester());
    }
}
