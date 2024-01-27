package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    @Override
    public boolean checkFriend(Member sender, Member receiver) {
        return friendRepository.existsBySenderAndReceiver(sender, receiver);
    }
}
