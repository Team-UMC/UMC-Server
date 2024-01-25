package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MemberService extends EntityLoader<Member, UUID> {
    MemberIdResponse updateMyProfile(Member member, MultipartFile profileImage, MemberUpdateMyProfileRequest request);
    MemberIdResponse updateProfile(Member member, UUID memberId, MemberUpdateProfileRequest request);
    MemberInquiryProfileResponse inquiryProfile(Member member, UUID memberId);
    MemberAuthenticateGithubResponse authenticateGithub(Member member, String code);
    MemberInquiryInfoWithPointResponse inquiryInfoWithPoint(Member member);
    MemberInquiryGithubResponse inquiryGithubImage(Member member);
    MemberInquiryPointsResponse inquiryMemberPoints(Member member);
    List<MemberSearchInfoResponse> searchMemberInfo(Member member, String keyword);
    void updateMemberActiveTime(UUID memberId);
    Member saveEntity(Member member);
}
