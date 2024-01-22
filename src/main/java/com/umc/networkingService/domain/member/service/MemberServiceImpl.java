package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.config.security.jwt.TokenInfo;
import com.umc.networkingService.domain.friend.service.FriendService;
import com.umc.networkingService.domain.member.client.*;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.*;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberPointRepository;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final KakaoMemberClient kakaoMemberClient;
    private final GoogleMemberClient googleMemberClient;
    private final NaverMemberClient naverMemberClient;
    private final AppleMemberClient appleMemberClient;
    private final MemberMapper memberMapper;

    private final MemberPointRepository memberPointRepository;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    private final SemesterPartService semesterPartService;
    private final MemberPositionService memberPositionService;
    private final FriendService friendService;

    private final S3FileComponent s3FileComponent;
    private final GithubMemberClient githubMemberClient;

    @Override
    public MemberLoginResponse socialLogin(String accessToken, SocialType socialType){
        // 로그인 구분
        if(socialType.equals(SocialType.KAKAO))
            return loginByKakao(accessToken);

        if(socialType.equals(SocialType.GOOGLE))
            return loginByGoogle(accessToken);

        if(socialType.equals(SocialType.NAVER))
            return loginByNaver(accessToken);

        if(socialType.equals(SocialType.APPLE))
            return loginByApple(accessToken);

        return null;
    }

    private MemberLoginResponse loginByApple(final String accessToken){
        // apple 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = appleMemberClient.getappleClientID(accessToken);
        //존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.APPLE);

        //1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()){
            return saveNewMember(clientId, SocialType.APPLE);
        }
        // 2. 있으면 : 새로운 토큰 반환
        return getNewToken(getMember.get());
    }

    private MemberLoginResponse loginByKakao(final String accessToken){
        // kakao 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = kakaoMemberClient.getkakaoClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.KAKAO);

        // 1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()) {
            return saveNewMember(clientId, SocialType.KAKAO);
        }
        // 2. 있으면 : 새로운 토큰 반환
        return getNewToken(getMember.get());
    }

    private MemberLoginResponse loginByNaver(final String accessToken){
        // naver 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = naverMemberClient.getnaverClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId,SocialType.NAVER);

        // 1. 없으면 (처음 로그인 하는 경우)
        if(getMember.isEmpty()) {
           return saveNewMember(clientId,SocialType.NAVER);
        }
        // 2. 있으면 (이미 로그인 했던 적이 있는 경우)
        return getNewToken(getMember.get());
    }

    private MemberLoginResponse loginByGoogle(final String accessToken){
        // google 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = googleMemberClient.getgoogleClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.GOOGLE);

        // 1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()){
            return saveNewMember(clientId, SocialType.GOOGLE);
        }
        // 2. 있으면 : 새로운 토큰 반환
        return getNewToken(getMember.get());
    }

    private MemberLoginResponse saveNewMember(String clientId, SocialType socialType) {
        Member member = memberMapper.toMember(clientId, socialType);
        Member newMember =  memberRepository.save(member);

        return getNewToken(newMember);
    }

    private MemberLoginResponse getNewToken(Member member) {
        // jwt 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId());
        // refreshToken 디비에 저장
        refreshTokenService.saveTokenInfo(tokenInfo.getRefreshToken(), member.getId());

        return memberMapper.toLoginMember(member, tokenInfo);
    }



    // 나의 프로필 업데이트 함수
    @Override
    @Transactional
    public MemberIdResponse updateMyProfile(Member loginMember, MultipartFile profileImage, MemberUpdateMyProfileRequest request) {

        Member member = loadEntity(loginMember.getId());

        String profileUrl = null;

        // 프로필 이미지 s3 저장
        if (profileImage != null)
            profileUrl = s3FileComponent.uploadFile("member", profileImage);

        // 수정된 정보 저장
        member.updateMemberInfo(request, profileUrl);

        return new MemberIdResponse(memberRepository.save(member).getId());
    }

    // 프로필 수정 함수(운영진용)
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
        memberPositionService.saveMemberPositionInfos(updateMember, request.getCampusPositions(), request.getCenterPositions());

        // 직책에 따른 Role 수정
        Role newRole = findMemberRole(updateMember.getPositions());
        updateMember.updateRole(newRole);

        // 특정 기수의 파트 변경
        semesterPartService.saveSemesterPartInfos(updateMember, request.getSemesterParts());

        return new MemberIdResponse(memberRepository.save(updateMember).getId());
    }

    // 프로필 조회 함수
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

        // 이외의 프로필 조회인 경우
        return memberMapper.toInquiryProfileResponse(inquiryMember, MemberRelation.OTHERS);
    }

    // 포인트 관련 정보 조회
    @Override
    public MemberInquiryInfoWithPointResponse inquiryInfoWithPoint(Member member) {
        // 소속 대학교 찾기
        University university = Optional.ofNullable(member.getUniversity())
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER_UNIVERSITY));

        // 본인 랭킹 구하기
        int rank = calculateMyRank(member, university);

        return memberMapper.toInquiryHomeInfoResponse(member, rank);
    }

    @Override
    @Transactional
    public MemberAuthenticationGithubResponse authenticationGithub(Member loginMember, String code) {
        Member member = loadEntity(loginMember.getId());

        String gitNickname = githubMemberClient.getGithubNickname(code);

        if (gitNickname == null || gitNickname.isBlank())
            throw new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION);

        member.authenticationGithub(gitNickname);

        Member savedMember = memberRepository.save(member);

        return new MemberAuthenticationGithubResponse(savedMember.getGitNickname());
    }

    @Override
    public MemberInquiryGithubResponse inquiryGithubImage(Member loginMember) {
        Member member = loadEntity(loginMember.getId());

        String gitNickName = member.getGitNickname();
        if (gitNickName == null)
            throw new RestApiException(ErrorCode.UNAUTHENTICATION_GITHUB);
        return new MemberInquiryGithubResponse("https://ghchart.rshah.org/2965FF/" + gitNickName);
    }

    @Override
    public MemberInquiryPointsResponse inquiryMemberPoints(Member member) {
        Page<MemberPoint> usedPointsPage = memberPointRepository.
                findAllByMemberOrderByCreatedAtDesc(member, PageRequest.of(0, 2));
        List<MemberInquiryPointsResponse.UsedHistory> usedHistories = usedPointsPage.stream()
                .map(MemberPoint::getPointType)
                .map(memberMapper::toUsedHistory)
                .toList();

        return memberMapper.toInquiryPointsResponse(member.getRemainPoint(), usedHistories);
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
