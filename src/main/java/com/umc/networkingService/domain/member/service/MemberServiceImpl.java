package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.friend.service.FriendService;
import com.umc.networkingService.domain.member.client.GithubMemberClient;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.*;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberPositionRepository;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final MemberPositionRepository memberPositionRepository;

    private final UniversityService universityService;
    private final BranchUniversityService branchUniversityService;
    private final FriendService friendService;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final S3FileComponent s3FileComponent;

    private final GithubMemberClient githubMemberClient;

    @Override
    @Transactional
    public MemberIdResponse signUp(Member member, MemberSignUpRequest request) {

        // 소속 대학교 탐색
        University university = universityService.findUniversityByName(request.getUniversityName());

        // 멤버 기본 정보 저장
        setMemberInfo(member, request, university);

        // 멤버 직책 저장
        saveMemberPositions(member, request.getCampusPositions(), request.getCenterPositions());

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
        deleteRefreshToken(member);
        return new MemberIdResponse(member.getId());
    }

    @Override
    @Transactional
    public MemberIdResponse withdrawal(Member member) {
        // 멤버 soft delete
        Member savedMember = loadEntity(member.getId());
        savedMember.delete();

        // refreshToken 삭제
        deleteRefreshToken(member);

        return new MemberIdResponse(member.getId());
    }

    @Override
    @Transactional
    public MemberIdResponse updateMyProfile(Member member, MultipartFile profileImage, MemberUpdateMyProfileRequest request) {

        String profileUrl = null;

        // 프로필 이미지 s3 저장
        if (profileImage != null)
            profileUrl = s3FileComponent.uploadFile("member", profileImage);

        // 수정된 정보 저장
        member.updateMemberInfo(request, profileUrl);

        return new MemberIdResponse(memberRepository.save(member).getId());
    }

    @Override
    @Transactional
    public MemberIdResponse updateProfile(Member member, UUID memberId, MemberUpdateProfileRequest request) {

        // 수정할 유저 탐색
        Member updateMember = loadEntity(memberId);

        // 직책 수정
        if (updateMember.getRole().getPriority() <= member.getRole().getPriority()) {
            // 본인보다 높거나 같은 직책의 운영진의 정보를 수정하려 할 경우
            throw new RestApiException(ErrorCode.UNAUTHORIZED_UPDATE_MEMBER);
        }

        if (member.getRole().getPriority() > 2 && !request.getCenterPositions().isEmpty()) {
            // 학교, 지부 운영진이 중앙 직책을 수정하려는 경우
            throw new RestApiException(ErrorCode.UNAUTHORIZED_UPDATE_CENTER_POSITION);
        }
        saveMemberPositions(updateMember, request.getCampusPositions(), request.getCenterPositions());

        // 파트, 학기 수정
        updateMember.updateMemberInfo(request.getParts(), request.getSemesters());

        return new MemberIdResponse(updateMember.getId());
    }

    @Override
    public MemberInquiryProfileResponse inquiryProfile(Member member, UUID memberId) {
        // 본인 프로필 조회인 경우
        if (memberId == null) {
            return memberMapper.toInquiryProfileResponse(member, MemberRelation.MINE);
        }

        Member inquiryMember = loadEntity(memberId);

        // 친구 프로필 조회인 경우
        if (friendService.checkFriend(member, inquiryMember)) {
            return memberMapper.toInquiryProfileResponse(inquiryMember, MemberRelation.FRIEND);
        }

        return memberMapper.toInquiryProfileResponse(inquiryMember, MemberRelation.OTHERS);
    }

    @Override
    public MemberInquiryHomeInfoResponse inquiryHomeInfo(Member member) {
        // 소속 대학교 찾기
        University university = Optional.ofNullable(member.getUniversity())
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER_UNIVERSITY));

        // 본인 랭킹 구하기
        int rank = calculateMyRank(member, university);

        return memberMapper.toInquiryHomeInfoResponse(member, rank);
    }

    @Override
    @Transactional
    public MemberAuthenticationGithubResponse authenticationGithub(Member member, String code) {

        String gitNickname = githubMemberClient.getGithubNickname(code);

        if (gitNickname == null || gitNickname.isBlank())
            throw new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION);

        member.authenticationGithub(gitNickname);

        Member savedMember = memberRepository.save(member);

        return new MemberAuthenticationGithubResponse(savedMember.getNickname());
    }

    @Override
    public MemberInquiryGithubResponse inquiryGithubImage(Member member) {
        String gitNickName = member.getGitNickname();
        if (gitNickName == null)
            throw new RestApiException(ErrorCode.UNAUTHENTICATION_GITHUB);
        return new MemberInquiryGithubResponse("https://ghchart.rshah.org/2965FF/" + gitNickName);
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
    private void saveMemberPositions(Member member, List<String> campusPositions, List<String> centerPositions) {

        // 기존 직책 삭제
        member.getPositions()
                .forEach(MemberPosition::delete);

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

    // member 객체를 이용한 refreshToken 삭제 함수
    private void deleteRefreshToken(Member member) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByMemberId(member.getId());

        refreshToken.ifPresent(refreshTokenService::delete);
    }

    // 교내 랭킹을 계산하는 함수
    private int calculateMyRank(Member member, University university) {
        List<Member> universityMembers = memberRepository.findAllByUniversityOrderByContributionPointDesc(university);

        int myRank = 1;
        Long prevPoint = -1L;
        int count = 0;

        for (Member universityMember : universityMembers) {
            if (!universityMember.getContributionPoint().equals(prevPoint)) {
                prevPoint = universityMember.getContributionPoint();
                myRank = myRank + count;
                count = 1;
            } else {
                count++;
            }

            if (universityMember.getId().equals(member.getId())) {
                return myRank;
            }
        }

        // 학교 구성원 중 멤버를 찾지 못하였을 경우
        throw new RestApiException(ErrorCode.EMPTY_MEMBER_UNIVERSITY);
    }

    @Override
    public Member loadEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER));
    }
}
