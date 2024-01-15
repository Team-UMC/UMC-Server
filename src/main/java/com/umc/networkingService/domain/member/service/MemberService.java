package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.SocialType;

public interface MemberService {
    public MemberLoginResponse socialLogin(final String accessToken, SocialType socialType);
}
