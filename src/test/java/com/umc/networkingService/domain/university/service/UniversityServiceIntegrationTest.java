package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.mascot.repository.MascotRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.PointType;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.university.dto.request.UniversityRequest;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("University 서비스의 ")
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class UniversityServiceIntegrationTest extends ServiceIntegrationTestConfig {

    @Autowired
    UniversityServiceImpl universityService;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    MascotRepository mascotRepository;

    @Test
    @DisplayName("전체 대학교 조회 - 성공")
    @Transactional (readOnly = true)
    public void joinUniversityListTest_Success() {
        //given

        //when
        UniversityResponse.JoinUniversities universityList = universityService.joinUniversityList();

        //then
        assertThat(universityList.getJoinUniversities().size()).isEqualTo(universityRepository.findAll().size());
        assertThat(universityList.getJoinUniversities().get(0).getUniversityName()).isEqualTo(university.getName());
    }

    @Test
    @DisplayName("우리 학교 세부 정보 조회 - 성공")
    @Transactional (readOnly = true)
    public void joinUniversityDetailTest_Success() {
        //given

        //when
        UniversityResponse.joinUniversityDetail universityDetail = universityService.joinUniversityDetail(member);

        //then
        assertThat(universityDetail.getUniversityName()).isEqualTo(university.getName());
    }

    @Test
    @DisplayName("전체 학교 랭킹 조회 - 성공")
    @Transactional
    public void joinUniversityRankingListTest_Success() {
        //given
        universityRepository.save(
                University.builder()
                        .name("1위")
                        .totalPoint(100L)
                        .build()
        );

        //when
        UniversityResponse.JoinUniversityRanks universityRankList = universityService.joinUniversityRankingList();

        //then
        assertThat(universityRankList.getJoinUniversityRanks().size()).isEqualTo(2);
        assertEquals(universityRankList.getJoinUniversityRanks().get(0).getUniversityName(), "1위");
        assertEquals(universityRankList.getJoinUniversityRanks().get(1).getUniversityName(),university.getName());
    }

    @Test
    @DisplayName("우리 학교 전체 기여도 랭킹 조회 - 성공")
    @Transactional
    public void joinContributionRankingListTest_Success() {
        //given
        memberRepository.save(
                Member.builder()
                        .name("2위")
                        .nickname("2위")
                        .clientId("222222")
                        .socialType(SocialType.KAKAO)
                        .role(Role.MEMBER)
                        .university(university)
                        .remainPoint(50L)
                        .build()
        );

        //when
        UniversityResponse.JoinContributionRanks contributionRankList = universityService.joinContributionRankingList(member);

        //then
        assertThat(contributionRankList.getJoinContributionRanks().size()).isEqualTo(2);
        assertEquals(contributionRankList.getJoinContributionRanks().get(0).getName(), member.getName());
        assertEquals("2위", contributionRankList.getJoinContributionRanks().get(1).getName());
    }

    @Test
    @DisplayName("우리 학교 마스코트 조회 - 성공")
    @Transactional
    public void joinUniversityMascotTest_Success() {
        //given
        System.out.println(member.getUniversity().getMascot().getDialogues());
        //when
        UniversityResponse.joinUniversityMascot universityMascot = universityService.joinUniversityMascot(member);

        //then
        assertThat(universityMascot.getMascotDialog()).isEqualTo(mascot.getDialogues());
    }

    @Test
    @DisplayName("우리 학교 마스코트 먹이주기 - 성공")
    @Transactional
    public void feedUniversityMascotTest_Success() {
        //given
        System.out.println(member.getRemainPoint());
        System.out.println(member.getUniversity().getTotalPoint());

        //when
        universityService.feedUniversityMascot(member, PointType.DOUGHNUT);

        //then
        assertThat(member.getRemainPoint()).isEqualTo(100L - PointType.DOUGHNUT.getPoint());
        assertThat(member.getUniversity().getTotalPoint()).isEqualTo(PointType.DOUGHNUT.getPoint());
    }

    @Test
    @DisplayName("우리 학교 마스코트 먹이주기 - 실패 - 포인트 부족")
    @Transactional
    public void feedUniversityMascotTest_Fail_NotEnoughPoint() {
        //given
        member.usePoint(100L);

        //when

        //then
        assertThrows(RestApiException.class, () -> universityService.feedUniversityMascot(member, PointType.DOUGHNUT));
    }

    @Test
    @DisplayName("학교 생성 - 성공")
    @Transactional
    public void createUniversityTest_Success() {
        //given
        UniversityRequest.universityInfo request = UniversityRequest.universityInfo.builder()
                .universityName("테스트대학교")
                .universityLogo(null)
                .semesterLogo(null)
                .build();

        //when
        UniversityResponse.UniversityId universityId = universityService.createUniversity(request);

        //then
        assertThat(universityRepository.findById(universityId.getUniversityId()).get().getName())
                .isEqualTo(request.getUniversityName());
    }

    @Test
    @DisplayName("학교 삭제 - 성공")
    @Transactional
    public void deleteUniversityTest_Success() {
        //given

        //when
        universityService.deleteUniversity(university.getId());

        //then
        assertFalse(universityRepository.existsById(university.getId()));
    }

    @Test
    @DisplayName("학교 정보 수정 - 성공")
    @Transactional
    public void patchUniversityTest_Success() {
        //given
        UniversityRequest.universityInfo request = UniversityRequest.universityInfo.builder()
                .universityName("테스트대학교")
                .build();

        //when
        universityService.patchUniversity(request, university.getId());

        //then
        assertEquals(universityRepository.findById(university.getId()).get().getName(),request.getUniversityName());
    }



}
