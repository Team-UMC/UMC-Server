package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    MemberIdResponse signUp(Member member, MemberSignUpRequest request);
    MemberGenerateNewAccessTokenResponse generateNewAccessToken(String refreshToken, Member member);
    MemberIdResponse logout(Member member);
    MemberIdResponse withdrawal(Member member);
    MemberIdResponse updateMyProfile(Member member, MultipartFile profileImage, MemberUpdateMyProfileRequest request);
}
