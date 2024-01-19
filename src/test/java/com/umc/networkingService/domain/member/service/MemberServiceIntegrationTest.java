package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.utils.S3FileComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member 서비스의 ")
@SpringBootTest
public class MemberServiceIntegrationTest {

    @Autowired private MemberService memberService;

    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private BranchUniversityRepository branchUniversityRepository;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private RefreshTokenService refreshTokenService;

    @MockBean private S3FileComponent s3FileComponent;

    private Member member;
    private String refreshToken;
    private University university;
    private Branch branch;
    private BranchUniversity branchUniversity;

    @BeforeEach
    public void setUp() {
        member = createMember();
        setToken(member.getId());
        university = createUniversity();
        branch = createBranch();
        branchUniversity = createBranchUniversity();
    }

    private Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .clientId("123456")
                        .socialType(SocialType.KAKAO)
                        .role(Role.MEMBER)
                        .build()
        );
    }

    private void setToken(UUID memberId) {
        refreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        refreshTokenService.saveTokenInfo(refreshToken, memberId);
    }

    private University createUniversity() {
        return universityRepository.save(
                University.builder()
                        .name("인하대학교")
                        .build()
        );
    }

    private Branch createBranch() {
        return branchRepository.save(
                Branch.builder()
                        .name("GACI")
                        .description("가치 지부입니다.")
                        .semester(Semester.FIFTH)
                        .build()
        );
    }

    private BranchUniversity createBranchUniversity() {
        return branchUniversityRepository.save(
                BranchUniversity.builder()
                        .branch(branch)
                        .university(university)
                        .isActive(Boolean.TRUE)
                        .build()
        );
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
                .parts(List.of(Part.SPRING))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .campusPositions(List.of("회장"))
                .centerPositions(List.of("Server 파트장"))
                .build();

        // when
        memberService.signUp(member, request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals("김준석", savedMember.getName());
        assertEquals("벡스", savedMember.getNickname());
        assertEquals("GACI", savedMember.getBranch().getName());
        assertEquals("인하대학교", savedMember.getUniversity().getName());
        assertEquals(1, savedMember.getPart().size());
        assertEquals(3, savedMember.getSemester().size());
    }

    @Test
    @DisplayName("access 토큰 재발급 테스트")
    public void generateNewAccessTokenTest() {
        // when
        MemberGenerateNewAccessTokenResponse response = memberService.generateNewAccessToken(refreshToken, member);

        // then
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logoutTest() {
        // when
        memberService.logout(member);

        // then
        assertFalse(refreshTokenService.findByMemberId(member.getId()).isPresent());
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    @Transactional
    public void withdrawalTest() {
        // when
        memberService.withdrawal(member);

        // then
        assertFalse(memberRepository.findById(member.getId()).isPresent());
        assertFalse(refreshTokenService.findByMemberId(member.getId()).isPresent());
    }

    @Test
    @DisplayName("나의 프로필 수정 테스트(이미지가 없는 경우)")
    @Transactional
    public void updateMyProfileWithoutImage() {
        // given
        MemberUpdateMyProfileRequest request = MemberUpdateMyProfileRequest.builder()
                .name("김준석")
                .nickname("준써크")
                .statusMessage("이번 기수 화이팅~")
                .build();

        // when
        memberService.updateMyProfile(member, null, request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals("김준석", savedMember.getName());
        assertEquals("이번 기수 화이팅~", savedMember.getStatusMessage());
    }

    @Test
    @DisplayName("나의 프로필 수정 테스트(이미지가 있는 경우)")
    @Transactional
    public void updateMyProfileWithImage() {
        // given
        MemberUpdateMyProfileRequest request = MemberUpdateMyProfileRequest.builder()
                .name("김준석")
                .nickname("준써크")
                .statusMessage("이번 기수 화이팅~")
                .build();

        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "profile.png",
                "image/png",
                "이미지 데이터".getBytes()
        );

        // 테스트에서는 s3 파일 생성 X
        given(s3FileComponent.uploadFile(any(), any())).willReturn("s3 url");

        // when
        memberService.updateMyProfile(member, profileImage, request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals("김준석", savedMember.getName());
        assertEquals("이번 기수 화이팅~", savedMember.getStatusMessage());
        assertEquals("s3 url", savedMember.getProfileImage());
    }
}
