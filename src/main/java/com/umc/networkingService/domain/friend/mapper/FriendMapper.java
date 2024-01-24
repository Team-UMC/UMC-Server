package com.umc.networkingService.domain.friend.mapper;

import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class FriendMapper {

    public Friend toFriend(Member sender, Member receiver) {
        return Friend.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
