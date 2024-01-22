package com.umc.networkingService.domain.member.mapper;

import com.umc.networkingService.config.security.jwt.TokenInfo;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.global.common.enums.Role;
import org.springframework.stereotype.Component;;
import com.umc.networkingService.domain.member.entity.MemberPosition;
import com.umc.networkingService.domain.member.entity.PositionType;

@Component
public class MemberMapper {
    public Member toMember(final String clientId, SocialType socialType){
        return Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .role(Role.MEMBER)
                .build();
    }

    public MemberLoginResponse toLoginMember(final Member member, TokenInfo tokenInfo) {
        return MemberLoginResponse.builder()
                .memberId(member.getId())
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }
    public MemberPosition toMemberPosition(Member member, PositionType type, String position) {
        return MemberPosition.builder()
                .name(position)
                .member(member)
                .type(type)
                .build();
    }
}
