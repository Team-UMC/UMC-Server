package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.client.GoogleMemberClient;
import com.umc.networkingService.domain.member.client.KakaoMemberClient;
import com.umc.networkingService.domain.member.client.NaverMemberClient;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Auth 서비스의 ")
public class AuthServiceIntegrationTest extends ServiceIntegrationTestConfig {

    @Autowired AuthService authService;

    @MockBean private KakaoMemberClient kakaoMemberClient;
    @MockBean private GoogleMemberClient googleMemberClient;
    @MockBean private NaverMemberClient naverMemberClient;

    @Test
    @DisplayName("카카오 로그인 테스트")
    @Transactional
    public void kakaoLoginTest() {
        // given
        String accessToken = "ya29.a0AfB_byD6";
        String clientId = "abcdefg";

        given(kakaoMemberClient.getkakaoClientID(any())).willReturn(clientId);

        //when
        MemberLoginResponse response = authService.socialLogin(accessToken, SocialType.KAKAO);

        //then
        // 멤버 저장 상태 테스트
        Optional<Member> optionalMember = memberRepository.findById(response.getMemberId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(clientId, savedMember.getClientId());
        assertEquals(Role.MEMBER, savedMember.getRole());
        assertEquals(SocialType.KAKAO, savedMember.getSocialType());

        // 리프레시 토큰 저장 상태 테스트
        RefreshToken refreshToken = refreshTokenService.findByMemberId(savedMember.getId())
                .orElseThrow();
        assertEquals(response.getRefreshToken(), refreshToken.getRefreshToken());
    }

    @Test
    @DisplayName("구글 로그인 테스트")
    @Transactional
    public void googleLoginTest() {
        // given
        String accessToken = "ya29.a0AfB_byD6";
        String sub = "abcdefg";

        given(googleMemberClient.getgoogleClientID(any())).willReturn(sub);

        //when
        MemberLoginResponse response = authService.socialLogin(accessToken, SocialType.GOOGLE);

        //then
        // 멤버 저장 상태 테스트
        Optional<Member> optionalMember = memberRepository.findById(response.getMemberId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(sub, savedMember.getClientId());
        assertEquals(Role.MEMBER, savedMember.getRole());
        assertEquals(SocialType.GOOGLE, savedMember.getSocialType());

        // 리프레시 토큰 저장 상태 테스트
        RefreshToken refreshToken = refreshTokenService.findByMemberId(savedMember.getId())
                .orElseThrow();
        assertEquals(response.getRefreshToken(), refreshToken.getRefreshToken());
    }

    @Test
    @DisplayName("네이버 로그인 테스트")
    @Transactional
    public void naverLoginTest() {
        // given
        String accessToken = "ya29.a0AfB_byD6";
        String clientId = "abcdefg";

        given(naverMemberClient.getnaverClientID(any())).willReturn(clientId);

        //when
        MemberLoginResponse response = authService.socialLogin(accessToken, SocialType.NAVER);

        //then
        // 멤버 저장 상태 테스트
        Optional<Member> optionalMember = memberRepository.findById(response.getMemberId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(clientId, savedMember.getClientId());
        assertEquals(Role.MEMBER, savedMember.getRole());
        assertEquals(SocialType.NAVER, savedMember.getSocialType());

        // 리프레시 토큰 저장 상태 테스트
        RefreshToken refreshToken = refreshTokenService.findByMemberId(savedMember.getId())
                .orElseThrow();
        assertEquals(response.getRefreshToken(), refreshToken.getRefreshToken());
    }

    @Test
    @DisplayName("회원 가입 테스트")
    @Transactional
    public void signUpTest() {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member)))
                .campusPositions(List.of("Android 파트장"))
                .centerPositions(List.of())
                .build();

        // when
        authService.signUp(member, request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals("김준석", savedMember.getName());
        assertEquals("벡스", savedMember.getNickname());
        assertEquals("GACI", savedMember.getBranch().getName());
        assertEquals("인하대학교", savedMember.getUniversity().getName());
        assertEquals(2, savedMember.getSemesterParts().size());
        assertEquals(1, savedMember.getPositions().size());
    }

    @Test
    @DisplayName("access 토큰 재발급 테스트")
    public void generateNewAccessTokenTest() {
        // when
        MemberGenerateTokenResponse response = authService.generateNewAccessToken(refreshToken, member);

        // then
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logoutTest() {
        // when
        authService.logout(member);

        // then
        assertFalse(refreshTokenService.findByMemberId(member.getId()).isPresent());
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    @Transactional
    public void withdrawalTest() {
        // when
        authService.withdrawal(member);

        // then
        assertFalse(memberRepository.findById(member.getId()).isPresent());
        assertFalse(refreshTokenService.findByMemberId(member.getId()).isPresent());
    }
}
