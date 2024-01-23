package com.umc.networkingService.domain.schedule.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import com.umc.networkingService.domain.schedule.repository.ScheduleRepository;
import com.umc.networkingService.global.common.enums.Role;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ScheduleControllerTestConfig {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected JwtTokenProvider jwtTokenProvider;
    @MockBean protected ScheduleRepository scheduleRepository;

    protected Member member;
    protected String accessToken;
    protected String refreshToken;

    @BeforeEach
    public void setup() {
        member = createMember();
        setToken(member);
    }

    private Member createMember() {
        return Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .role(Role.MEMBER)
                .build();
    }

    private void setToken(Member member) {
        accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
    }
}
