package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberSignUpResponse;
import com.umc.networkingService.domain.member.entity.Member;

public interface MemberService {
    MemberSignUpResponse signUp(Member member, MemberSignUpRequest request);
}
