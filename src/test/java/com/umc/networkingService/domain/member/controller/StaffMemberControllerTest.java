package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.dto.response.MemberSearchInfoResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        member.updateRole(Role.CENTER_STAFF);
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
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(response.getMemberId().toString()))
                .andExpect(jsonPath("$.result.memberId").value(response.getMemberId().toString()));
    }

    @DisplayName("유저 검색 API 테스트")
    @Test
    public void searchMembersInfo() throws Exception {
        // given
        String keyword = "벡스/김준석";
        member.updateRole(Role.CENTER_STAFF);

        MemberSearchInfoResponse response = MemberSearchInfoResponse.builder()
                .memberId(UUID.randomUUID())
                .universityName("인하대학교")
                .campusPositions(List.of("회장"))
                .centerPositions(List.of())
                .semesterParts(List.of(
                        new SemesterPartInfo(Part.ANDROID, Semester.THIRD),
                        new SemesterPartInfo(Part.SPRING, Semester.FOURTH),
                        new SemesterPartInfo(Part.SPRING, Semester.FIFTH)
                )).build();

        given(memberService.searchMemberInfo(any(), eq(keyword))).willReturn(List.of(response));
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/staff/members/search")
                        .header("Authorization", accessToken)
                        .param("keyword", keyword))
                .andDo(print())  // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.length()").value(1));
    }
}
