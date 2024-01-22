package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import org.springframework.web.multipart.MultipartFile;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.SocialType;

import java.util.UUID;

public interface MemberService extends EntityLoader<Member, UUID> {
    MemberLoginResponse socialLogin(final String accessToken, SocialType socialType);
    MemberIdResponse updateMyProfile(Member member, MultipartFile profileImage, MemberUpdateMyProfileRequest request);
    MemberIdResponse updateProfile(Member member, UUID memberId, MemberUpdateProfileRequest request);
    MemberInquiryProfileResponse inquiryProfile(Member member, UUID memberId);
    MemberInquiryInfoWithPointResponse inquiryInfoWithPoint(Member member);
    MemberAuthenticationGithubResponse authenticationGithub(Member member, String code);
    MemberInquiryGithubResponse inquiryGithubImage(Member member);
    MemberInquiryPointsResponse inquiryMemberPoints(Member member);
}
