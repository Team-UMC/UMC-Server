package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.global.common.base.EntityLoader;

import java.util.UUID;


public interface AuthService extends EntityLoader<Member, UUID> {
    MemberLoginResponse socialLogin(final String accessToken, SocialType socialType);
    MemberIdResponse signUp(Member member, MemberSignUpRequest request);
    MemberGenerateTokenResponse generateNewAccessToken(String refreshToken, Member member);
    MemberIdResponse logout(Member member);
    MemberIdResponse withdrawal(Member member);
    MemberIdResponse saveNewDummyMember(String clientId);

}
