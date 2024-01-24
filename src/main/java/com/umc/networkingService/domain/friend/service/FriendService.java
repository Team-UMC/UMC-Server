package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.member.entity.Member;

import java.util.UUID;

public interface FriendService {
    FriendIdResponse createNewFriend(Member member, UUID memberId);
    FriendIdResponse deleteFriend(Member member, UUID memberId);
    boolean checkFriend(Member sender, Member receiver);
}
