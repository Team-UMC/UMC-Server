package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.dto.response.FriendInquiryByStatusResponse;
import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.AuthService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Friend 서비스의 ")
@SpringBootTest
public class FriendServiceIntegrationTest extends ServiceIntegrationTestConfig {

    @Autowired
    private FriendService friendService;
    @Autowired
    private AuthService authService;

    @Autowired
    private FriendRepository friendRepository;

    private void makeFriend(Member sender, Member receiver) {
        friendRepository.save(
                Friend.builder().sender(sender).receiver(receiver).build()
        );
    }

    private Member createAndSignupFriend(String id, Role role, String name, String nickname, List<String> titles, List<String> hashtags, LocalDateTime lastActiveTime) {
        Member friend = createMember(id, role);
        authService.signUp(friend, getInfoRequest(name, nickname, titles, hashtags));
        friend.updateLastActiveTime(lastActiveTime);
        return friend;
    }

    @Test
    @DisplayName("친구 추가 테스트")
    @Transactional
    public void createNewFriend() {
        // given
        Member friend = createMember("222222", Role.MEMBER);

        // when
        FriendIdResponse response = friendService.createNewFriend(member, friend.getId());

        // then
        Optional<Friend> optionalFriend = friendRepository.findById(response.getFriendId());
        assertTrue(optionalFriend.isPresent());
        Friend savedFriend = optionalFriend.get();

        assertEquals(member, savedFriend.getSender());
        assertEquals(friend, savedFriend.getReceiver());
    }

    @Test
    @DisplayName("친구 추가 테스트 - 이미 친구인 경우")
    @Transactional
    public void createNewFriendWithAlreadyFriend() {
        // given
        Member friend = createMember("222222", Role.MEMBER);
        friendRepository.save(
                Friend.builder().sender(member).receiver(friend).build()
        );

        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> friendService.createNewFriend(member, friend.getId()));

        // then
        assertEquals(ErrorCode.ALREADY_FRIEND_RELATION, exception.getErrorCode());
        assertTrue(friendRepository.existsBySenderAndReceiver(member, friend));
    }

    @Test
    @DisplayName("친구 삭제 테스트")
    @Transactional
    public void deleteFriend() {
        // given
        Member friend = createMember("222222", Role.MEMBER);
        makeFriend(member, friend);

        // when
        FriendIdResponse response = friendService.deleteFriend(member, friend.getId());

        // then
        Optional<Friend> optionalFriend = friendRepository.findById(response.getFriendId());
        assertNotNull(optionalFriend.get().getDeletedAt());
    }

    @Test
    @DisplayName("친구 삭제 테스트 - 친구가 아닌 상태")
    @Transactional
    public void deleteFriendWithNotFriend() {
        // given
        Member friend = createMember("222222", Role.MEMBER);

        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> friendService.deleteFriend(member, friend.getId()));

        // then
        assertEquals(ErrorCode.NOT_FRIEND_RELATION, exception.getErrorCode());
        assertFalse(friendRepository.existsBySenderAndReceiver(member, friend));
    }

    @Test
    @DisplayName("접속 중인 친구 조회 테스트")
    @Transactional
    public void inquiryFriendsByStatusOnline() {
        // given
        Member onlineFriend1 = createAndSignupFriend("222222", Role.CAMPUS_STAFF, "이경수", "리버", List.of("iOS 파트장"), List.of(), LocalDateTime.now());
        makeFriend(member, onlineFriend1);

        Member onlineFriend2 = createAndSignupFriend("333333", Role.MEMBER, "박재우", "다재", List.of(), List.of(), LocalDateTime.now());
        makeFriend(member, onlineFriend2);

        Member offlineFriend1 = createAndSignupFriend("444444", Role.MEMBER, "김보민", "밈보", List.of(), List.of(), LocalDateTime.now().minusMinutes(10));
        makeFriend(member, offlineFriend1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("receiver.nickname").ascending());

        // when
        FriendInquiryByStatusResponse response = friendService.inquiryFriendsByStatus(member, true, pageable);

        // then
        assertEquals(2, response.getFriends().size());
        assertEquals("다재", response.getFriends().get(0).getNickname());
        assertEquals("리버", response.getFriends().get(1).getNickname());
    }

    @Test
    @DisplayName("미접속 중인 친구 조회 테스트")
    @Transactional
    public void inquiryFriendsByStatusOffline() {
        // given
        Member onlineFriend1 = createAndSignupFriend("222222", Role.CAMPUS_STAFF, "이경수", "리버", List.of("iOS 파트장"), List.of(), LocalDateTime.now());
        makeFriend(member, onlineFriend1);

        Member onlineFriend2 = createAndSignupFriend("333333", Role.MEMBER, "박재우", "다재", List.of(), List.of(), LocalDateTime.now());
        makeFriend(member, onlineFriend2);

        Member offlineFriend1 = createAndSignupFriend("444444", Role.MEMBER, "김보민", "밈보", List.of(), List.of(), LocalDateTime.now().minusMinutes(10));
        makeFriend(member, offlineFriend1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("receiver.nickname").ascending());

        // when
        FriendInquiryByStatusResponse response = friendService.inquiryFriendsByStatus(member, false, pageable);

        // then
        assertEquals(1, response.getFriends().size());
        assertEquals("밈보", response.getFriends().get(0).getNickname());
    }
}
