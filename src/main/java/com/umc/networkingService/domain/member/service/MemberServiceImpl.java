package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberSignUpResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPosition;
import com.umc.networkingService.domain.member.entity.PositionType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final MemberPositionRepository memberPositionRepository;

    private final UniversityService universityService;
    private final BranchUniversityService branchUniversityService;

    @Override
    @Transactional
    public MemberSignUpResponse signUp(Member member, MemberSignUpRequest request) {

        // 소속 대학교 탐색
        University university = universityService.findUniversityByName(request.getUniversityName());

        // 멤버 기본 정보 저장
        setMemberInfo(member, request, university);

        // 멤버 직책 저장
        saveMemberPositions(member, request);

        return new MemberSignUpResponse(memberRepository.save(member).getId());
    }

    // 멤버 기본 정보 저장 함수
    private void setMemberInfo(Member member, MemberSignUpRequest request, University university) {
        member.setMemberInfo(
                request,
                findMemberRole(request),
                university,
                branchUniversityService.findBranchByUniversity(university)
        );
    }

    // 멤버 직책 정보 저장 함수
    private void saveMemberPositions(Member member, MemberSignUpRequest request) {
        saveMemberPosition(member, request.getCenterPositions(), PositionType.CENTER);
        saveMemberPosition(member, request.getCampusPositions(), PositionType.CAMPUS);
    }

    private void saveMemberPosition(Member member, List<String> positions, PositionType positionType) {
        List<MemberPosition> memberPositions = positions.stream()
                .map(position -> memberMapper.toMemberPosition(member, positionType, position))
                .toList();

        memberPositionRepository.saveAll(memberPositions);
    }

    // 멤버 Role 생성 함수
    private Role findMemberRole(MemberSignUpRequest request) {
        if (request.getCenterPositions().isEmpty()) {
            return findCampusRole(request.getCampusPositions());
        }

        return findCenterRole(request.getCenterPositions());
    }

    private Role findCampusRole(List<String> campusPositions) {
        if (campusPositions.isEmpty()) {
            return Role.MEMBER;
        }

        if (isExecutive(campusPositions)) {
            return Role.BRANCH_STAFF;
        }

        return Role.CAMPUS_STAFF;
    }

    private Role findCenterRole(List<String> centerPositions) {
        if (isExecutive(centerPositions)) {
            return Role.TOTAL_STAFF;
        }

        return Role.CENTER_STAFF;
    }

    // 회장, 부회장 판별 함수
    private boolean isExecutive(List<String> positions) {
        return positions.stream()
                .anyMatch(position -> position.equals("회장") || position.equals("부회장"));
    }
}
