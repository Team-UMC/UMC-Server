package com.umc.networkingService.domain.invite.controller;

import com.umc.networkingService.domain.invite.dto.response.InviteAuthenticateResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteInquiryMineResponse;
import com.umc.networkingService.domain.invite.service.InviteService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Invite 컨트롤러의")
public class InviteControllerTest extends ControllerTestConfig {

    @MockBean private InviteService inviteService;

    @DisplayName("초대 코드 발급 API 테스트")
    @Test
    public void createInviteCode() throws Exception {
        // given
        member.updateRole(Role.CAMPUS_STAFF);

        InviteCreateResponse response = new InviteCreateResponse("초대 코드", Role.MEMBER);

        given(inviteService.createInviteCode(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/staff/invites")
                        .param("role", Role.MEMBER.toString())
                        .header("Authorization", accessToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.inviteCode").value(response.getInviteCode().toString()));
    }

    @DisplayName("초대 코드 발급 API 테스트 - 일반 부원인 경우")
    @Test
    public void createInviteCodeWithMember() throws Exception {
        // given
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/staff/invites")
                        .param("role", Role.MEMBER.toString())
                        .header("Authorization", accessToken))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/access/denied"));
    }

    @DisplayName("나의 초대 코드 조회 API 테스트")
    @Test
    public void inquiryMyInviteCode() throws Exception {
        // given
        member.updateRole(Role.BRANCH_STAFF);

        InviteInquiryMineResponse response = new InviteInquiryMineResponse(
                List.of(
                        new InviteInquiryMineResponse.InviteInfo("초대 코드1", Role.MEMBER, LocalDateTime.now()),
                        new InviteInquiryMineResponse.InviteInfo("초대 코드2", Role.CAMPUS_STAFF, LocalDateTime.now()))
        );


        given(inviteService.inquiryMyInviteCode(any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/staff/invites")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.invites").value(hasSize(response.getInvites().size())));
    }

    @DisplayName("초대 코드 인증 API 테스트")
    @Test
    public void authenticateInviteCode() throws Exception {
        // given
        InviteAuthenticateResponse response = new InviteAuthenticateResponse(Role.BRANCH_STAFF);

        given(inviteService.authenticateInviteCode(member, "inviteCode")).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/invites/inviteCode")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.role").value(response.getRole().toString()));
    }
}
