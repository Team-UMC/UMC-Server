package com.umc.networkingService.domain.member.mapper;

import com.umc.networkingService.config.security.jwt.TokenInfo;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import org.springframework.stereotype.Component;;

@Component
public class MemberMapper {
    public Member toMember(final String clientId, SocialType socialType){
        return Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .build();
    }

    public MemberLoginResponse toLoginMember(final Member member, TokenInfo tokenInfo){
        return MemberLoginResponse.builder()
                .memberId(member.getId())
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }
}
