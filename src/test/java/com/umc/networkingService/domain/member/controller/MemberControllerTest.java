package com.umc.networkingService.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@DisplayName("Member 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @MockBean private MemberService memberService;

    @MockBean private MemberRepository memberRepository;

    private Member member;
    private String accessToken;
    private String refreshToken;

    @BeforeEach
    public void setUp() {
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

    @DisplayName("회원가입 API 테스트")
    @Test
    public void signUpTest() throws Exception {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .parts(List.of(Part.SPRING))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .campusPositions(List.of("회장"))
                .centerPositions(List.of("Server 파트장"))
                .build();

        MemberIdResponse response = new MemberIdResponse(member.getId());

        // when
        given(memberService.signUp(eq(member), any(MemberSignUpRequest.class))).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));


    }

    @DisplayName("Access 토큰 재발급 API 테스트")
    @Test
    public void generateNewAccessTokenTest() throws Exception {
        // given
        MemberGenerateNewAccessTokenResponse response = new MemberGenerateNewAccessTokenResponse("newAccessToken");

        given(memberService.generateNewAccessToken(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/members/token/refresh")
                        .header("refreshToken", refreshToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.accessToken").value(response.getAccessToken()));
    }

    @DisplayName("로그아웃 API 테스트")
    @Test
    public void logoutTest() throws Exception {
        // given
        MemberIdResponse response = new MemberIdResponse(member.getId());

        given(memberService.logout(any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(delete("/members/logout")
                        .header("Authorization", accessToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));
    }

    @DisplayName("회원 탈퇴 API 테스트")
    @Test
    public void withdrawalTest() throws Exception {
        // given
        MemberIdResponse response = new MemberIdResponse(member.getId());

        given(memberService.withdrawal(any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(delete("/members")
                        .header("Authorization", accessToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));
    }

    @DisplayName("나의 프로필 수정 API 테스트 - 프로필 이미지 없음")
    @Test
    public void updateMyProfileWithoutImage() throws Exception {
        // given
        MemberUpdateMyProfileRequest request = MemberUpdateMyProfileRequest.builder()
                .name("김준석")
                .nickname("준써크")
                .statusMessage("이번 기수 화이팅~")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile requestPart = new MockMultipartFile("request", "", "application/json", requestJson.getBytes(StandardCharsets.UTF_8));

        MemberIdResponse response = new MemberIdResponse(member.getId());

        given(memberService.updateMyProfile(any(), any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/members/update")
                        .file(requestPart)
                        .header("Authorization", accessToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));
    }

    @DisplayName("나의 프로필 수정 API 테스트 - 프로필 이미지 있음")
    @Test
    public void updateMyProfileWithImage() throws Exception {
        // given
        MemberUpdateMyProfileRequest request = MemberUpdateMyProfileRequest.builder()
                .name("김준석")
                .nickname("준써크")
                .statusMessage("이번 기수 화이팅~")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile requestPart = new MockMultipartFile("request", "", "application/json", requestJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "profileImage.jpg", "image/jpeg", "profile image".getBytes());

        MemberIdResponse response = new MemberIdResponse(member.getId());

        given(memberService.updateMyProfile(any(), any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/members/update")
                        .file(profileImage)
                        .file(requestPart)
                        .header("Authorization", accessToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));
    }
}