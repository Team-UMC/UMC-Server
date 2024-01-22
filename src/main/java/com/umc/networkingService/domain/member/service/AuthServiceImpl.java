package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final SemesterPartService semesterPartService;
    private final MemberPositionService memberPositionService;
    private final RefreshTokenService refreshTokenService;
    private final UniversityService universityService;
    private final BranchUniversityService branchUniversityService;

    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입을 수행하는 함수
    @Override
    @Transactional
    public MemberIdResponse signUp(Member member, MemberSignUpRequest request) {

        // 소속 대학교 탐색
        University university = universityService.findUniversityByName(request.getUniversityName());

        // 멤버 직책 저장
        memberPositionService.saveMemberPositionInfos(member, request.getCampusPositions(), request.getCenterPositions());

        // 기수별 파트 저장
        semesterPartService.saveSemesterPartInfos(member, request.getSemesterParts());

        // 이외의 기본 정보 저장
        member.setMemberInfo(request.getName(), request.getNickname(),
                university, branchUniversityService.findBranchByUniversity(university));

        return new MemberIdResponse(memberRepository.save(member).getId());
    }

    // 새로운 액세스 토큰 발급 함수
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

    // 로그아웃 함수
    @Override
    @Transactional
    public MemberIdResponse logout(Member member) {
        deleteRefreshToken(member);
        return new MemberIdResponse(member.getId());
    }

    // 회원 탈퇴 함수
    @Override
    @Transactional
    public MemberIdResponse withdrawal(Member member) {
        Member savedMember = loadEntity(member.getId());

        // refreshToken 삭제
        deleteRefreshToken(savedMember);

        // 멤버 soft delete
        savedMember.delete();

        return new MemberIdResponse(savedMember.getId());
    }

    // member 객체를 이용한 refreshToken 삭제 함수
    private void deleteRefreshToken(Member member) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByMemberId(member.getId());

        refreshToken.ifPresent(refreshTokenService::delete);
    }

    // 멤버 로드 함수
    @Override
    public Member loadEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER));
    }
}
