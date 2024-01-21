package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPosition;
import com.umc.networkingService.domain.member.entity.PositionType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberPositionServiceImpl implements MemberPositionService {

    private final MemberPositionRepository memberPositionRepository;
    private final MemberMapper memberMapper;

    // 생성자 주입 또는 @Autowired로 repository와 mapper 객체를 주입받습니다.

    // 멤버 직책 정보 저장 함수
    @Override
    public void saveMemberPositions(Member member, List<String> campusPositions, List<String> centerPositions) {
        // 기존 직책 삭제
        member.getPositions().forEach(MemberPosition::delete);

        List<MemberPosition> memberPositions = new ArrayList<>();
        memberPositions.addAll(saveMemberPosition(member, campusPositions, PositionType.CAMPUS));
        memberPositions.addAll(saveMemberPosition(member, centerPositions, PositionType.CENTER));

        member.updatePositions(memberPositions);
    }

    private List<MemberPosition> saveMemberPosition(Member member, List<String> positions, PositionType positionType) {
        List<MemberPosition> memberPositions = positions.stream()
                .map(position -> memberMapper.toMemberPosition(member, positionType, position))
                .toList();

        memberPositionRepository.saveAll(memberPositions);

        return memberPositions;
    }
}
