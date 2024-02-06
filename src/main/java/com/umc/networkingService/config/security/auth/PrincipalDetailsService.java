package com.umc.networkingService.config.security.auth;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.global.common.exception.code.MemberErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new RestApiException(MemberErrorCode.EMPTY_MEMBER));
        return new PrincipalDetails(memberEntity);
    }
}