package com.umc.networkingService.domain.friend.repository;

import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {
    Optional<Friend> findBySenderAndReceiver(Member sender, Member receiver);
    boolean existsBySenderAndReceiver(Member sender, Member receiver);

    @Query("SELECT f.receiver FROM Friend f WHERE f.sender = :member AND " +
            "(:status = true AND f.receiver.lastActiveTime >= :fiveMinutesAgo OR " +
            ":status = false AND f.receiver.lastActiveTime < :fiveMinutesAgo)")
    Page<Member> findFriendsByStatus(@Param("member") Member member,
                                     @Param("status") boolean status,
                                     @Param("fiveMinutesAgo") LocalDateTime fiveMinutesAgo,
                                     Pageable pageable);
}
