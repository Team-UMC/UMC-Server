package com.umc.networkingService.domain.friend.mapper;

import com.umc.networkingService.domain.friend.dto.response.FriendInquiryByStatusResponse;
import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendMapper {

    public Friend toFriend(Member sender, Member receiver) {
        return Friend.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public FriendInquiryByStatusResponse.FriendInfo toFriendInfo(Member member,
                                                                 List<String> campusPositions,
                                                                 List<String> centerPositions,
                                                                 List<SemesterPartInfo> semesterParts) {
        return FriendInquiryByStatusResponse.FriendInfo.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .universityName(member.getUniversity().getName())
                .campusPositions(campusPositions)
                .centerPositions(centerPositions)
                .semesterParts(semesterParts)
                .build();

    }
}
