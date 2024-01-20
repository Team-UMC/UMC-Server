package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth 서비스의 ")
@SpringBootTest
public class AuthServiceIntegrationTest extends MemberServiceTestConfig {

    @Autowired AuthService authService;


    @Test
    @DisplayName("회원 가입 테스트")
    @Transactional
    public void signUpTest() {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .parts(List.of(Part.SPRING))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH))
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
        assertEquals(1, savedMember.getParts().size());
        assertEquals(2, savedMember.getSemesters().size());
        assertEquals(1, savedMember.getPositions().size());
    }

    @Test
    @DisplayName("access 토큰 재발급 테스트")
    public void generateNewAccessTokenTest() {
        // when
        MemberGenerateNewAccessTokenResponse response = authService.generateNewAccessToken(refreshToken, member);

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
