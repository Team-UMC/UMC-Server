package com.umc.networkingService.domain.friend.service;


import com.umc.networkingService.domain.member.dto.response.MemberInquiryProfileResponse;
import com.umc.networkingService.domain.member.entity.Member;

import java.util.UUID;

public interface FriendShipService {
    MemberInquiryProfileResponse inquiryProfile(Member member, UUID memberId);
    }
