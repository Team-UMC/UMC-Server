package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;

public interface MemberService {
    MemberIdResponse signUp(Member member, MemberSignUpRequest request);
    MemberGenerateNewAccessTokenResponse generateNewAccessToken(String refreshToken, Member member);
    MemberIdResponse logout(Member member);
}
