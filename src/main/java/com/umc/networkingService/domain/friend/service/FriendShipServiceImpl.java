package com.umc.networkingService.domain.friend.service;

import com.umc.networkingService.domain.friend.repository.FriendRepository;
import com.umc.networkingService.domain.member.dto.response.MemberInquiryProfileResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberRelation;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendShipServiceImpl implements FriendShipService{
    private final FriendService friendService;
    private final MemberService memberService;

    private final MemberMapper memberMapper;

    // 프로필 조회 함수
    @Override
    public MemberInquiryProfileResponse inquiryProfile(Member member, UUID memberId) {

        Member loginMember = memberService.loadEntity(member.getId());
        // 본인 프로필 조회인 경우
        if (memberId == null || loginMember.getId().equals(memberId)) {
            return memberMapper.toInquiryProfileResponse(loginMember, MemberRelation.MINE);
        }

        Member inquiryMember = memberService.loadEntity(memberId);

        // 친구 프로필 조회인 경우
        if (friendService.checkFriend(loginMember, inquiryMember)) {
            return memberMapper.toInquiryProfileResponse(inquiryMember, MemberRelation.FRIEND);
        }

        // 이외의 프로필 조회인 경우
        return memberMapper.toInquiryProfileResponse(inquiryMember, MemberRelation.OTHERS);
    }

}
