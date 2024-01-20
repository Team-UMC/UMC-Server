package com.umc.networkingService.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("StaffMember 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class StaffMemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @MockBean private MemberService memberService;
    @MockBean private MemberRepository memberRepository;

    private Member member;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        member = createMember();
        setToken(member);
    }

    private Member createMember() {
        return Member.builder()
                .id(UUID.randomUUID())
                .clientId("111111")
                .socialType(SocialType.KAKAO)
                .role(Role.MEMBER)
                .build();
    }

    private void setToken(Member member) {
        accessToken = jwtTokenProvider.generateAccessToken(member.getId());
    }

    // 에러 해결해야함
    @DisplayName("유저 정보 수정 API 테스트")
    @Test
    public void updateProfileTest() throws Exception {
        // given
        UUID memberId = UUID.randomUUID();
        MemberUpdateProfileRequest request = MemberUpdateProfileRequest.builder()
                .campusPositions(List.of())
                .centerPositions(List.of("회장"))
                .parts(List.of(Part.SPRING, Part.ANDROID))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .build();

        MemberIdResponse response = new MemberIdResponse(member.getId());
        given(memberService.updateProfile(any(Member.class), any(UUID.class), any(MemberUpdateProfileRequest.class))).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/staff/members/" + memberId + "/update")
                        .with(user(member.getClientId())) // RequestPostProcessor를 사용하여 요청에 인증 정보 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
