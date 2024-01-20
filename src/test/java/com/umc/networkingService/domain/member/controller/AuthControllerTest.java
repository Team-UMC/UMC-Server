package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.service.AuthService;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Auth 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest extends MemberControllerTestConfig {

    @MockBean
    private AuthService authService;

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
        given(authService.signUp(eq(member), any(MemberSignUpRequest.class))).willReturn(response);
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

        given(authService.generateNewAccessToken(any(), any())).willReturn(response);
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

        given(authService.logout(any())).willReturn(response);
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

        given(authService.withdrawal(any())).willReturn(response);
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
}
