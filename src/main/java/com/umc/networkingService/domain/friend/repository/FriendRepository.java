package com.umc.networkingService.domain.friend.repository;

import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {
    boolean existsBySenderAndReceiver(Member sender, Member receiver);
}
