package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.dto.response.FriendInquiryByStatusResponse;
import com.umc.networkingService.domain.friend.entity.Friend;
import com.umc.networkingService.domain.friend.mapper.FriendMapper;
import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.PositionType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;
    private final MemberMapper memberMapper;

    private final MemberService memberService;

    // 친구 추가 함수
    @Override
    public FriendIdResponse createNewFriend(Member member, UUID memberId) {
        Member loginMember = memberService.loadEntity(member.getId());

        Member friendMember = memberService.loadEntity(memberId);

        if (friendRepository.existsBySenderAndReceiver(loginMember, friendMember))
            throw new RestApiException(ErrorCode.ALREADY_FRIEND_RELATION);

        return createAndSaveNewFriend(loginMember, friendMember);
    }

    // 친구 삭제 함수
    @Override
    public FriendIdResponse deleteFriend(Member member, UUID memberId) {
        Member loginMember = memberService.loadEntity(member.getId());

        Member friendMember = memberService.loadEntity(memberId);

        // 존재하지 않을 경우 예외 처리
        Friend friend = friendRepository.findBySenderAndReceiver(loginMember, friendMember)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FRIEND_RELATION));

        friend.delete();

        return new FriendIdResponse(friend.getId());
    }

    @Override
    public FriendInquiryByStatusResponse inquiryFriendsByStatus(Member member, boolean status, Pageable pageable) {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        Page<Member> friends = friendRepository.findFriendsByStatus(member, status, fiveMinutesAgo, pageable);

        List<FriendInquiryByStatusResponse.FriendInfo> friendInfos = friends.stream()
                .map(friend -> friendMapper.toFriendInfo(
                        friend,
                        memberService.getPositionNamesByType(friend, PositionType.CAMPUS),
                        memberService.getPositionNamesByType(friend, PositionType.CENTER),
                        memberMapper.toSemesterPartInfos(friend.getSemesterParts())
                )).toList();


        return new FriendInquiryByStatusResponse(friendInfos, friends.hasNext());
    }

    // 친구 여부 확인 함수
    @Override
    public boolean checkFriend(Member sender, Member receiver) {
        return friendRepository.existsBySenderAndReceiver(sender, receiver);
    }

    private FriendIdResponse createAndSaveNewFriend(Member loginMember, Member friendMember) {
        Friend friend = friendMapper.toFriend(loginMember, friendMember);
        UUID savedFriendId = friendRepository.save(friend).getId();

        return new FriendIdResponse(savedFriendId);
    }
}
