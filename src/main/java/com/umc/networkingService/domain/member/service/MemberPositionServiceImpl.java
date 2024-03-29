package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPosition;
import com.umc.networkingService.domain.member.entity.PositionType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import com.umc.networkingService.global.common.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberPositionServiceImpl implements MemberPositionService {

    private final MemberPositionRepository memberPositionRepository;
    private final MemberMapper memberMapper;

    // 멤버 직책 정보 저장 함수
    @Override
    public void saveMemberPositionInfos(Member member, List<String> campusPositions, List<String> centerPositions) {
        // 기존 직책 삭제
        memberPositionRepository.deleteAll(findMemberPositionByMember(member));

        List<MemberPosition> memberPositions = new ArrayList<>();
        memberPositions.addAll(saveMemberPositions(member, campusPositions, PositionType.CAMPUS));
        memberPositions.addAll(saveMemberPositions(member, centerPositions, PositionType.CENTER));

        // 직책에 따른 Role 수정
        Role newRole = findMemberRole(memberPositions);
        member.updateRole(newRole);
    }

    @Override
    public List<MemberPosition> findMemberPositionByMember(Member member) {
        return memberPositionRepository.findAllByMember(member);
    }

    // 멤버의 대표 직책 반환 함수
    @Override
    public String findRepresentativePosition(Member member) {
        List<MemberPosition> memberPositions = memberPositionRepository.findAllByMember(member);

        // 대표 Position 선정 방식
        // 1. CENTER > CAMPUS
        // 2. 회장, 부회장 > 나머지
        Optional<String> centerPositionName = findPositionByNameAndType(memberPositions, PositionType.CENTER);
        return centerPositionName
                .orElseGet(() -> findPositionByNameAndType(memberPositions, PositionType.CAMPUS).orElse("부원"));
    }

    private Optional<String> findPositionByNameAndType(List<MemberPosition> memberPositions, PositionType type) {
        return memberPositions.stream()
                .filter(mp -> mp.getType() == type)
                .min(Comparator.comparing(mp -> !(mp.getName().equals("회장") || mp.getName().equals("부회장"))))
                .map(MemberPosition::getName);
    }


    // 멤버 직책을 디비에 저장하는 함수
    private List<MemberPosition> saveMemberPositions(Member member, List<String> positions, PositionType positionType) {
        List<MemberPosition> memberPositions = positions.stream()
                .map(position -> memberMapper.toMemberPosition(member, positionType, position))
                .toList();

        memberPositionRepository.saveAll(memberPositions);

        return memberPositions;
    }

    // 멤버의 새로운 Role 찾기 함수
    private Role findMemberRole(List<MemberPosition> memberPositions) {
        if (memberPositions.isEmpty()) {
            return Role.MEMBER;
        }

        List<MemberPosition> centerPositions = findPositionsByType(memberPositions, PositionType.CENTER);

        if (!centerPositions.isEmpty()) {
            if (isExecutive(centerPositions))
                return Role.TOTAL_STAFF;
            return Role.CENTER_STAFF;
        }

        List<MemberPosition> campusPositions = findPositionsByType(memberPositions, PositionType.CAMPUS);

        if (isExecutive(campusPositions))
            return Role.BRANCH_STAFF;
        return Role.CAMPUS_STAFF;
    }

    // 특정 타입의 직책을 반환하는 함수
    private List<MemberPosition> findPositionsByType(List<MemberPosition> memberPositions, PositionType type) {
        return memberPositions.stream()
                .filter(memberPosition -> memberPosition.getType() == type)
                .toList();
    }

    // 회장, 부회장 판별 함수
    private boolean isExecutive(List<MemberPosition> positions) {
        return positions.stream()
                .anyMatch(position -> position.getName().equals("회장") || position.getName().equals("부회장"));
    }
}
