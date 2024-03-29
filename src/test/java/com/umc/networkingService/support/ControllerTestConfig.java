package com.umc.networkingService.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.global.common.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestConfig {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected JwtTokenProvider jwtTokenProvider;
    @MockBean protected MemberRepository memberRepository;

    protected Member member;
    protected String accessToken;
    protected String refreshToken;

    @BeforeEach
    public void setUp() {
        member = createMember("111111", Role.MEMBER);
        setToken(member);
    }

    protected Member createMember(String clientId, Role role) {
        return Member.builder()
                .id(UUID.randomUUID())
                .clientId(clientId)
                .socialType(SocialType.KAKAO)
                .role(role)
                .build();
    }

    protected void setToken(Member member) {
        accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
    }
}
