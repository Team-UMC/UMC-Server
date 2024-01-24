package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Friend 서비스의 ")
@SpringBootTest
public class FriendServiceIntegrationTest extends ServiceIntegrationTestConfig {

    @Autowired
    private FriendService friendService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private FriendRepository friendRepository;

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
    @DisplayName("친구 추가 테스트")
    @Transactional
    public void createNewFriendWithAlreadyFriend() {
        // given
        Member friend = createMember("222222", Role.MEMBER);

        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> friendService.createNewFriend(member, friend.getId()));

        // then
        assertEquals(ErrorCode.ALREADY_FRIEND_RELATION, exception.getErrorCode());
        assertFalse(friendRepository.existsBySenderAndReceiver(member, friend));
    }
}
