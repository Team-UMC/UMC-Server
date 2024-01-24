package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.client.GithubMemberClient;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.*;
import com.umc.networkingService.domain.member.repository.MemberPointRepository;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Member 서비스의 ")
@SpringBootTest
public class MemberServiceIntegrationTest extends ServiceIntegrationTestConfig {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberPositionRepository memberPositionRepository;
    @Autowired
    private MemberPointRepository memberPointRepository;


    @MockBean private S3FileComponent s3FileComponent;
    @MockBean private GithubMemberClient githubMemberClient;



    private MemberPoint createMemberPoint(PointType pointType) {
        return MemberPoint.builder()
                .member(member)
                .pointType(pointType)
                .build();
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
                .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member)))
                .build();

        Optional<MemberPosition> optionalMemberPosition = member.getPositions().stream().findFirst();
        assertTrue(optionalMemberPosition.isPresent());
        UUID memberPositionId = optionalMemberPosition.get().getId();

        // when
        memberService.updateProfile(staff, member.getId(), request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals(1, savedMember.getPositions().size());
        assertEquals(2, savedMember.getSemesterParts().size());
        assertEquals(Role.BRANCH_STAFF, savedMember.getRole());
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
                .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member)))
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
        assertEquals(0, savedMember.getSemesterParts().size());
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
                .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member)))
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
        assertEquals(0, savedMember.getSemesterParts().size());
    }

    @Test
    @DisplayName("포인트 관련 유저 정보 조회 테스트")
    @Transactional
    public void inquiryHomeInfo() {
        // given
        authService.signUp(member, getInfoRequest());
        member.updateContributionPoint(1000L);

        Member universityMember1 = createMember("222222", Role.MEMBER);
        authService.signUp(universityMember1, getInfoRequest());
        universityMember1.updateContributionPoint(2000L);
        Member universityMember2 = createMember("333333", Role.MEMBER);
        authService.signUp(universityMember2, getInfoRequest());
        universityMember2.updateContributionPoint(2000L);
        Member universityMember3 = createMember("444444", Role.MEMBER);
        authService.signUp(universityMember3, getInfoRequest());
        universityMember3.updateContributionPoint(3000L);
        Member universityMember4 = createMember("555555", Role.MEMBER);
        authService.signUp(universityMember4, getInfoRequest());
        universityMember4.updateContributionPoint(1000L);

        // when
        MemberInquiryInfoWithPointResponse response = memberService.inquiryInfoWithPoint(member);

        // then
        assertEquals("벡스", response.getNickname());
        assertEquals(1000L, response.getContributionPoint());
        assertEquals(4, response.getContributionRank());
    }

    @Test
    @DisplayName("깃허브 연동 테스트")
    @Transactional
    public void authenticateGithub() {
        // given

        // 실제 깃허브 서버와 통신 x
        given(githubMemberClient.getGithubNickname(any())).willReturn("junseokkim");

        // when
        MemberAuthenticateGithubResponse response = memberService.authenticateGithub(member, "깃허브 인가 코드");

        // then
        assertEquals("junseokkim", response.getGithubNickname());
    }

    @Test
    @DisplayName("깃허브 데이터 조회 테스트")
    @Transactional
    public void inquiryGithubImage() {
        // given
        member.authenticateGithub("junseokkim");

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
        assertEquals(ErrorCode.UNAUTHENTICATED_GITHUB, exception.getErrorCode());

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
        try {
            memberPointRepository.save(createMemberPoint(PointType.PUDDING));
            memberPointRepository.save(createMemberPoint(PointType.PUDDING));
            Thread.sleep(1000);  // 1초 대기
            memberPointRepository.save(createMemberPoint(PointType.DOUGHNUT));
        } catch (InterruptedException ignored) {
        }

        // when
        MemberInquiryPointsResponse response = memberService.inquiryMemberPoints(member);

        // then
        assertNull(response.getRemainPoint());
        List<Long> points = response.getUsedHistories().stream()
                .map(MemberInquiryPointsResponse.UsedHistory::getPoint)
                .toList();
        assertEquals(List.of(10L, 5L), points);
    }

    @Test
    @DisplayName("운영진용 유저 검색 테스트")
    @Transactional
    public void searchMemberInfo() {
        // given
        String keyword = "벡스/김준석";
        authService.signUp(member, getInfoRequest());

        Member staff = createMember("222222", Role.CENTER_STAFF);

        // when
        List<MemberSearchInfoResponse> responses = memberService.searchMemberInfo(staff, keyword);

        // then
        assertEquals(1, responses.size());
        assertEquals(member.getId(), responses.get(0).getMemberId());
    }

    @Test
    @DisplayName("운영진용 유저 검색 테스트 - 여러명인 경우")
    @Transactional
    public void searchMembersInfo() {
        // given
        String keyword = "벡스/김준석";

        authService.signUp(member, getInfoRequest());

        Member anotherMember = createMember("222222", Role.CAMPUS_STAFF);
        authService.signUp(anotherMember, getInfoRequest());

        Member staff = createMember("333333", Role.CENTER_STAFF);

        // when
        List<MemberSearchInfoResponse> responses = memberService.searchMemberInfo(staff, keyword);

        // then
        assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("운영진용 유저 검색 테스트 - 상위 운영진 미조회")
    @Transactional
    public void searchMembersInfoByLowRole() {
        // given
        String keyword = "벡스/김준석";

        authService.signUp(member, getInfoRequest());

        Member anotherMember = createMember("222222", Role.TOTAL_STAFF);
        authService.signUp(anotherMember, getInfoRequest());

        Member staff = createMember("333333", Role.CENTER_STAFF);

        // when
        List<MemberSearchInfoResponse> responses = memberService.searchMemberInfo(staff, keyword);

        // then
        assertEquals(1, responses.size());
    }


}