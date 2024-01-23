package com.umc.networkingService.domain.member.controller;


import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.MemberRelation;
import com.umc.networkingService.domain.member.entity.PointType;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member 컨트롤러의")
public class MemberControllerTest extends ControllerTestConfig {

    @Autowired private MemberMapper memberMapper;

    @MockBean private MemberService memberService;

    @DisplayName("나의 프로필 수정 API 테스트 - 프로필 이미지 없음")
    @Test
    public void updateMyProfileWithoutImageTest() throws Exception {
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
    public void updateMyProfileWithImageTest() throws Exception {
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

    @DisplayName("유저 프로필 조회 API 테스트 - 본인")
    @Test
    public void inquiryMyProfileTest() throws Exception {
        // given
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().semester(Semester.THIRD).part(Part.ANDROID).build(),
                SemesterPart.builder().semester(Semester.FOURTH).part(Part.SPRING).build()
        );

        MemberInquiryProfileResponse response = MemberInquiryProfileResponse.builder()
                .memberId(member.getId())
                .profileImage("profileImage")
                .universityName("인하대학교")
                .name("김준석")
                .nickname("벡스")
                .semesterParts(memberMapper.toSemesterPartInfos(semesterParts))
                .statusMessage("아자아자 화이팅")
                .owner(MemberRelation.MINE)
                .build();

        given(memberService.inquiryProfile(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/members")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));
    }

    @DisplayName("유저 프로필 조회 API 테스트 - 타인")
    @Test
    public void inquiryOthersProfileTest() throws Exception {
        // given
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().semester(Semester.THIRD).part(Part.ANDROID).build(),
                SemesterPart.builder().semester(Semester.FOURTH).part(Part.SPRING).build()
        );

        MemberInquiryProfileResponse response = MemberInquiryProfileResponse.builder()
                .memberId(member.getId())
                .profileImage("profileImage")
                .universityName("인하대학교")
                .name("김준석")
                .nickname("벡스")
                .semesterParts(memberMapper.toSemesterPartInfos(semesterParts))
                .statusMessage("아자아자 화이팅")
                .owner(MemberRelation.OTHERS)
                .build();

        given(memberService.inquiryProfile(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/members/" + member.getId())
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(member.getId().toString()));
    }

    @DisplayName("포인트 관련 유저 정보 조회 API 테스트")
    @Test
    public void inquiryHomeInfoTest() throws Exception {
        // given
        MemberInquiryInfoWithPointResponse response = MemberInquiryInfoWithPointResponse.builder()
                .profileImage("프로필 이미지")
                .nickname("벡스")
                .contributionPoint(1000L)
                .contributionRank(2)
                .build();

        given(memberService.inquiryInfoWithPoint(any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/members/rank")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.contributionPoint").value("1000"));
    }

    @DisplayName("깃허브 연동 API 테스트")
    @Test
    public void authenticateGithub() throws Exception {
        // given
        MemberAuthenticateGithubResponse response = new MemberAuthenticateGithubResponse("junseokkim");

        given(memberService.authenticateGithub(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/members/github")
                        .param("code", "깃허브 인가 코드")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.githubNickname").value(response.getGithubNickname().toString()));
    }


    @DisplayName("깃허브 데이터 조회 API 테스트")
    @Test
    public void inquiryGithubImage() throws Exception {
        // given
        MemberInquiryGithubResponse response = new MemberInquiryGithubResponse(
                "https://ghchart.rshah.org/2965FF/junseokkim");

        given(memberService.inquiryGithubImage(any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/members/github")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.githubImage").value(response.getGithubImage().toString()));
    }

    @DisplayName("나의 남은 포인트 및 사용 내역 조회 API 테스트")
    @Test
    public void inquiryMemberPoints() throws Exception {
        // given
        List<MemberInquiryPointsResponse.UsedHistory> usedHistories = Stream.of(PointType.PUDDING, PointType.DOUGHNUT)
                .map(memberMapper::toUsedHistory)
                .toList();

        MemberInquiryPointsResponse response = MemberInquiryPointsResponse.builder()
                .remainPoint(1000L)
                .usedHistories(usedHistories)
                .build();

        given(memberService.inquiryMemberPoints(any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/members/points")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.remainPoint").value(response.getRemainPoint()))
                .andExpect(jsonPath("$.result.usedHistories", hasSize(response.getUsedHistories().size())));
    }
}