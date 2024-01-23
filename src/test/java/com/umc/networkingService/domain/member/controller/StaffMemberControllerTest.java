package com.umc.networkingService.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.support.ControllerTestConfig;
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
public class StaffMemberControllerTest extends ControllerTestConfig {

    @Autowired private MemberMapper memberMapper;
    @MockBean private MemberService memberService;
    // 에러 해결해야함
    @DisplayName("유저 정보 수정 API 테스트")
    @Test
    public void updateProfileTest() throws Exception {
        // given
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().semester(Semester.THIRD).part(Part.ANDROID).build(),
                SemesterPart.builder().semester(Semester.FOURTH).part(Part.SPRING).build()
        );

        UUID memberId = UUID.randomUUID();
        MemberUpdateProfileRequest request = MemberUpdateProfileRequest.builder()
                .campusPositions(List.of())
                .centerPositions(List.of("회장"))
                .semesterParts(memberMapper.toSemesterPartInfos(semesterParts))
                .build();

        MemberIdResponse response = new MemberIdResponse(member.getId());

        given(memberService.updateProfile(any(Member.class), any(UUID.class), any(MemberUpdateProfileRequest.class))).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/staff/members/" + memberId + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
