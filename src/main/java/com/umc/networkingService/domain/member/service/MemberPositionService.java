package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.entity.Member;

import java.util.List;

public interface MemberPositionService {
    void saveMemberPositions(Member member, List<String> campusPositions, List<String> centerPositions);

}
