package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.dto.response.MemberInquiryProfileResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberRelation;
import com.umc.networkingService.domain.member.service.AuthService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FriendShip 서비스의 ")
@SpringBootTest
public class FriendShipServiceImplIntegrationTest extends ServiceIntegrationTestConfig {
    @Autowired
    private FriendShipService friendShipService;
    @Autowired
    private AuthService authService;

    @Autowired
    private FriendRepository friendRepository;

    @Test
    @DisplayName("유저 프로필 조회 테스트 - 본인")
    @Transactional
    public void inquiryMyProfile() {
        // given
        authService.signUp(member, getInfoRequest("김준석", "벡스", List.of("회장"), List.of()));

        // when
        MemberInquiryProfileResponse response = friendShipService.inquiryProfile(member, null);

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
        Member loginMember = createMember("222222", Role.CAMPUS_STAFF);

        authService.signUp(member, getInfoRequest("김준석", "벡스", List.of("회장"), List.of()));

        friendRepository.save(Friend.builder()
                .sender(loginMember)
                .receiver(member)
                .build());

        // when
        MemberInquiryProfileResponse response = friendShipService.inquiryProfile(loginMember, member.getId());

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
        Member loginMember = createMember("222222", Role.CAMPUS_STAFF);

        authService.signUp(member, getInfoRequest("김준석", "벡스", List.of("회장"), List.of()));

        // when
        MemberInquiryProfileResponse response = friendShipService.inquiryProfile(loginMember, member.getId());

        // then
        assertEquals("김준석", response.getName());
        assertEquals("인하대학교", response.getUniversityName());
        assertEquals(MemberRelation.OTHERS, response.getOwner());
    }

}
