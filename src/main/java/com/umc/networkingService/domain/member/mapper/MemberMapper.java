package com.umc.networkingService.domain.member.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toMember(final String clientId, SocialType socialType) {
        return Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .build();
    }
}
