package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPosition;
import com.umc.networkingService.domain.member.entity.PositionType;
import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final MemberPositionRepository memberPositionRepository;

    private final UniversityService universityService;
    private final BranchUniversityService branchUniversityService;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public MemberIdResponse signUp(Member member, MemberSignUpRequest request) {

        // 소속 대학교 탐색
        University university = universityService.findUniversityByName(request.getUniversityName());

        // 멤버 기본 정보 저장
        setMemberInfo(member, request, university);

        // 멤버 직책 저장
        saveMemberPositions(member, request);

        return new MemberIdResponse(memberRepository.save(member).getId());
    }

    @Override
    @Transactional
    public MemberGenerateNewAccessTokenResponse generateNewAccessToken(String refreshToken, Member member) {

        RefreshToken savedRefreshToken = refreshTokenService.findByMemberId(member.getId())
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_MEMBER_JWT));

        // 디비에 저장된 refreshToken과 동일하지 않다면 유효하지 않음
        if (!refreshToken.equals(savedRefreshToken.getRefreshToken()))
            throw new RestApiException(ErrorCode.INVALID_REFRESH_TOKEN);

        return new MemberGenerateNewAccessTokenResponse(jwtTokenProvider.generateAccessToken(member.getId()));
    }

    @Override
    @Transactional
    public MemberIdResponse logout(Member member) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByMemberId(member.getId());

        refreshToken.ifPresent(refreshTokenService::delete);

        return new MemberIdResponse(member.getId());
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
