package com.umc.networkingService.config.security.auth;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER));
        return new PrincipalDetails(memberEntity);
    }
}