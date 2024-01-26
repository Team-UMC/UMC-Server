package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.PositionType;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MemberService extends EntityLoader<Member, UUID> {
    MemberIdResponse updateMyProfile(Member member, MultipartFile profileImage, MemberUpdateMyProfileRequest request);
    MemberIdResponse updateProfile(Member member, UUID memberId, MemberUpdateProfileRequest request);
    MemberAuthenticateGithubResponse authenticateGithub(Member member, String code);
    MemberInquiryInfoWithPointResponse inquiryInfoWithPoint(Member member);
    MemberInquiryGithubResponse inquiryGithubImage(Member member);
    MemberInquiryPointsResponse inquiryMemberPoints(Member member);
    MemberSearchInfosResponse searchMemberInfo(Member member, String keyword);
    void updateMemberActiveTime(UUID memberId);
    List<String> getPositionNamesByType(Member member, PositionType type);
    Member saveEntity(Member member);
}
