package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.*;
import com.umc.networkingService.domain.member.repository.MemberPointRepository;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.BDDMockito.given;

@DisplayName("Member 서비스의 ")
@SpringBootTest public class MemberServiceIntegrationTest {

    @Autowired private MemberService memberService;

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberPositionRepository memberPositionRepository;
    @Autowired private MemberPointRepository memberPointRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private BranchUniversityRepository branchUniversityRepository;
    @Autowired private FriendRepository friendRepository;
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
        member = createMember("111111", Role.MEMBER);
        setToken(member.getId());
        university = createUniversity();
        branch = createBranch();
        branchUniversity = createBranchUniversity();
    }

    private Member createMember(String clientId, Role role) {
        return memberRepository.save(
                Member.builder()
                        .clientId(clientId)
                        .socialType(SocialType.KAKAO)
                        .role(role)
                        .build()
        );
    }

    private void setInfo(Member nowMember) {
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .parts(List.of(Part.SPRING))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH))
                .campusPositions(List.of("Android 파트장"))
                .centerPositions(List.of())
                .build();

        nowMember.setMemberInfo(request, Role.BRANCH_STAFF, university, branch);
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

    private MemberPoint createMemberPoint(PointType pointType) {
        return MemberPoint.builder()
                .member(member)
                .pointType(pointType)
                .build();
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
                .semesters(List.of(Semester.THIRD, Semester.FOURTH))
                .campusPositions(List.of("Android 파트장"))
                .centerPositions(List.of())
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
        assertEquals(1, savedMember.getParts().size());
        assertEquals(2, savedMember.getSemesters().size());
        assertEquals(1, savedMember.getPositions().size());
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

    @Test
    @DisplayName("프로필 수정 테스트")
    @Transactional
    public void updateProfile() {
        // given

        Member staff = createMember("222222", Role.TOTAL_STAFF);

        member.updatePositions(List.of(
                memberPositionRepository.save(MemberPosition.builder()
                        .member(member)
                        .type(PositionType.CAMPUS)
                        .name("Android 파트장")
                        .build())
        ));

        MemberUpdateProfileRequest request = MemberUpdateProfileRequest.builder()
                .campusPositions(List.of("회장"))
                .centerPositions(List.of())
                .parts(List.of(Part.SPRING, Part.ANDROID))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .build();

        Optional<MemberPosition> optionalMemberPosition = member.getPositions().stream().findFirst();
        assertTrue(optionalMemberPosition.isPresent());
        UUID memberPositionId = optionalMemberPosition.get().getId();

        // when
        memberService.updateProfile(staff, member.getId(),request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(1, savedMember.getPositions().size());
        assertEquals(2, savedMember.getParts().size());
        assertEquals(3, savedMember.getSemesters().size());
        assertFalse(memberPositionRepository.findByIdAndDeletedAtIsNull(memberPositionId).isPresent());
    }

    @Test
    @DisplayName("프로필 수정 권한 예외 테스트 - 상위 운영진 정보 수정 시")
    @Transactional
    public void updateProfileWithUpdateMember() {
        // given
        Member staff = createMember("222222", Role.CAMPUS_STAFF);
        member.updateRole(Role.TOTAL_STAFF);

        MemberUpdateProfileRequest request = MemberUpdateProfileRequest.builder()
                .campusPositions(List.of("회장"))
                .centerPositions(List.of())
                .parts(List.of(Part.SPRING, Part.ANDROID))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .build();


        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> memberService.updateProfile(staff, member.getId(), request));

        // then
        assertEquals(ErrorCode.UNAUTHORIZED_UPDATE_MEMBER, exception.getErrorCode());

        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(0, savedMember.getPositions().size());
        assertEquals(0, savedMember.getParts().size());
        assertEquals(0, savedMember.getSemesters().size());
    }

    @Test
    @DisplayName("프로필 수정 권한 예외 테스트 - 학교, 지부 운영진이 중앙 직책 부여 시")
    @Transactional
    public void updateProfileWithUpdateCenterPosition() {
        // given
        Member staff = createMember("222222", Role.CAMPUS_STAFF);

        MemberUpdateProfileRequest request = MemberUpdateProfileRequest.builder()
                .campusPositions(List.of())
                .centerPositions(List.of("회장"))
                .parts(List.of(Part.SPRING, Part.ANDROID))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .build();


        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> memberService.updateProfile(staff, member.getId(), request));

        // then
        assertEquals(ErrorCode.UNAUTHORIZED_UPDATE_CENTER_POSITION, exception.getErrorCode());

        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(0, savedMember.getPositions().size());
        assertEquals(0, savedMember.getParts().size());
        assertEquals(0, savedMember.getSemesters().size());
    }

    @Test
    @DisplayName("유저 프로필 조회 테스트 - 본인")
    @Transactional
    public void inquiryMyProfile() {
        // given
        setInfo(member);

        // when
        MemberInquiryProfileResponse response = memberService.inquiryProfile(member, null);

        // then
        assertEquals("김준석", response.getName());
        assertEquals("인하대학교", response.getUniversityName());
        assertEquals(MemberRelation.MINE, response.getOwner());
    }

    @Test
    @DisplayName("유저 프로필 조회 테스트 - 친구")
    @Transactional
    public void inquiryFriendProfile() {
        // given
        setInfo(member);

        Member loginMember = createMember("222222", Role.CAMPUS_STAFF);

        friendRepository.save(Friend.builder()
                .sender(loginMember)
                .receiver(member)
                .build());

        // when
        MemberInquiryProfileResponse response = memberService.inquiryProfile(loginMember, member.getId());

        // then
        assertEquals("김준석", response.getName());
        assertEquals("인하대학교", response.getUniversityName());
        assertEquals(MemberRelation.FRIEND, response.getOwner());
    }

    @Test
    @DisplayName("유저 프로필 조회 테스트 - 그 외")
    @Transactional
    public void inquiryOthersProfile() {
        // given
        setInfo(member);

        Member loginMember = createMember("222222", Role.CAMPUS_STAFF);

        // when
        MemberInquiryProfileResponse response = memberService.inquiryProfile(loginMember, member.getId());

        // then
        assertEquals("김준석", response.getName());
        assertEquals("인하대학교", response.getUniversityName());
        assertEquals(MemberRelation.OTHERS, response.getOwner());
    }

    @Test
    @DisplayName("유저 홈화면 정보 조회 테스트")
    @Transactional
    public void inquiryHomeInfo() {
        // given
        setInfo(member);
        member.updateContributionPoint(1000L);

        Member universityMember1 = createMember("222222", Role.MEMBER);
        setInfo(universityMember1);
        universityMember1.updateContributionPoint(2000L);
        Member universityMember2 = createMember("333333", Role.MEMBER);
        setInfo(universityMember2);
        universityMember2.updateContributionPoint(2000L);
        Member universityMember3 = createMember("444444", Role.MEMBER);
        setInfo(universityMember3);
        universityMember3.updateContributionPoint(3000L);
        Member universityMember4 = createMember("555555", Role.MEMBER);
        setInfo(universityMember4);
        universityMember4.updateContributionPoint(1000L);

        // when
        MemberInquiryHomeInfoResponse response = memberService.inquiryHomeInfo(member);

        // then
        assertEquals("벡스", response.getNickname());
        assertEquals(1000L, response.getContributionPoint());
        assertEquals(4, response.getContributionRank());
    }

    @Test
    @DisplayName("깃허브 데이터 조회 테스트")
    @Transactional
    public void inquiryGithubImage() {
        // given
        member.authenticationGithub("junseokkim");

        // when
        MemberInquiryGithubResponse response = memberService.inquiryGithubImage(member);

        // then
        assertEquals("https://ghchart.rshah.org/2965FF/junseokkim", response.getGithubImage());
    }

    @Test
    @DisplayName("깃허브 데이터 조회 테스트 - 연동 안된 경우")
    @Transactional
    public void inquiryGithubImageWithException() {
        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> memberService.inquiryGithubImage(member));

        // then
        assertEquals(ErrorCode.UNAUTHENTICATION_GITHUB, exception.getErrorCode());

        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertNull(savedMember.getGitNickname());
    }

    @Test
    @DisplayName("나의 남은 포인트 및 사용 내역 조회 테스트 - 1개")
    @Transactional
    public void inquiryMemberPoints() {
        // given
        memberPointRepository.save(MemberPoint.builder()
                .member(member)
                .pointType(PointType.PUDDING)
                .build());

        // when
        MemberInquiryPointsResponse response = memberService.inquiryMemberPoints(member);

        // then
        assertNull(response.getRemainPoint());
        assertEquals(1, response.getUsedHistories().size());
    }

    @Test
    @DisplayName("나의 남은 포인트 및 사용 내역 조회 테스트 - 2개 이상")
    @Transactional
    public void inquiryMemberPointsWithMultiple() {
        // given
        memberPointRepository.saveAll(List.of(
                createMemberPoint(PointType.PUDDING),
                createMemberPoint(PointType.PUDDING),
                createMemberPoint(PointType.DOUGHNUT)
        ));

        // when
        MemberInquiryPointsResponse response = memberService.inquiryMemberPoints(member);

        // then
        assertNull(response.getRemainPoint());
        List<Long> points = response.getUsedHistories().stream()
                .map(MemberInquiryPointsResponse.UsedHistory::getPoint)
                .toList();
        assertEquals(List.of(10L, 5L), points);
    }
}
