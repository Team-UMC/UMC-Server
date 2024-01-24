package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.dto.response.FriendInquiryByStatusResponse;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FriendService {
    FriendIdResponse createNewFriend(Member member, UUID memberId);
    FriendIdResponse deleteFriend(Member member, UUID memberId);
    FriendInquiryByStatusResponse inquiryFriendsByStatus(Member member, boolean status, Pageable pageable);
    boolean checkFriend(Member sender, Member receiver);
}
