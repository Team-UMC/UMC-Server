package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.member.entity.Member;

public interface FriendService {
    boolean checkFriend(Member sender, Member receiver);
}
