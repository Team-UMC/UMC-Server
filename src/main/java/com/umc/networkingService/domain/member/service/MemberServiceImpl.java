package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.domain.member.client.GithubMemberClient;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{


    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final MemberPointRepository memberPointRepository;

    private final SemesterPartService semesterPartService;
    private final MemberPositionService memberPositionService;

    private final S3FileComponent s3FileComponent;
    private final GithubMemberClient githubMemberClient;

    // 나의 프로필 업데이트 함수
    @Override
    @Transactional
    public MemberIdResponse updateMyProfile(Member member, MultipartFile profileImage, MemberUpdateMyProfileRequest request) {

        Member loginMember = loadEntity(member.getId());

        String profileUrl = null;

        // 프로필 이미지 s3 저장
        if (profileImage != null)
            profileUrl = s3FileComponent.uploadFile("Member", profileImage);

        // 수정된 정보 저장
        loginMember.updateMemberInfo(request, profileUrl);

        return new MemberIdResponse(memberRepository.save(loginMember).getId());
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

    // 포인트 관련 정보 조회
    @Override
    public MemberInquiryInfoWithPointResponse inquiryInfoWithPoint(Member member) {

        Member loginMember = loadEntity(member.getId());

        // 소속 대학교 찾기
        University university = Optional.ofNullable(loginMember.getUniversity())
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER_UNIVERSITY));

        // 본인 랭킹 구하기
        int rank = calculateMyRank(loginMember, university);

        return memberMapper.toInquiryHomeInfoResponse(loginMember, rank);
    }

    // 깃허브 인증 함수
    @Override
    @Transactional
    public MemberAuthenticateGithubResponse authenticateGithub(Member member, String code) {

        Member loginMember = loadEntity(member.getId());

        String gitNickname = githubMemberClient.getGithubNickname(code);

        if (gitNickname == null || gitNickname.isBlank())
            throw new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION);

        loginMember.authenticateGithub(gitNickname);

        Member savedMember = memberRepository.save(loginMember);

        return new MemberAuthenticateGithubResponse(savedMember.getGitNickname());
    }

    // 깃허브 잔디 이미지 조회 함수
    @Override
    public MemberInquiryGithubResponse inquiryGithubImage(Member member) {
        Member loginMember = loadEntity(member.getId());

        String gitNickName = loginMember.getGitNickname();
        if (gitNickName == null)
            throw new RestApiException(ErrorCode.UNAUTHENTICATED_GITHUB);
        return new MemberInquiryGithubResponse("https://ghchart.rshah.org/2965FF/" + gitNickName);
    }

    // 포인트 관련 멤버 정보 조회 함수
    @Override
    public MemberInquiryPointsResponse inquiryMemberPoints(Member member) {
        Member loginMember = loadEntity(member.getId());

        Page<MemberPoint> usedPointsPage = memberPointRepository.
                findAllByMemberOrderByCreatedAtDesc(loginMember, PageRequest.of(0, 2));
        List<MemberInquiryPointsResponse.UsedHistory> usedHistories = usedPointsPage.stream()
                .map(MemberPoint::getPointType)
                .map(memberMapper::toUsedHistory)
                .toList();

        return memberMapper.toInquiryPointsResponse(loginMember.getRemainPoint(), usedHistories);
    }

    // 멤버 검색 함수(운영진용)
    @Override
    public MemberSearchInfosResponse searchMemberInfo(Member loginMember, String keyword) {
        Member member = loadEntity(loginMember.getId());

        // keyword 양식 검증
        String[] nicknameAndName = validateKeyword(keyword);

        // 해당 유저가 본인보다 상위 운영진인 경우 검색 대상에서 제외
        List<Member> searchedMembers = memberRepository.findAllByNicknameAndName(nicknameAndName[0], nicknameAndName[1]).stream()
                        .filter(searchedMember -> searchedMember.getRole().getPriority() > loginMember.getRole().getPriority())
                        .toList();

        List<MemberSearchInfosResponse.MemberInfo> memberInfos = searchedMembers.stream()
                .map(searchedMember -> memberMapper.toSearchMembersResponse(
                        searchedMember,
                        getPositionNamesByType(searchedMember, PositionType.CAMPUS),
                        getPositionNamesByType(searchedMember, PositionType.CENTER)
                )).toList();

        return new MemberSearchInfosResponse(memberInfos);
    }

    @Override
    @Transactional
    public void updateMemberActiveTime(UUID memberId) {
        Member loginMember = loadEntity(memberId);

        loginMember.updateLastActiveTime(LocalDateTime.now());
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

    // 올바른 키워드인지 확인
    private String[] validateKeyword(final String keyword) {
        // {닉네임/이름} 양식 검증
        String[] splits = keyword.split("/");
        if (splits.length != 2)
            throw new RestApiException(ErrorCode.INVALID_MEMBER_KEYWORD);
        return splits;
    }

    // 타입에 따른 직책 찾기 함수
    private List<String> getPositionNamesByType(Member member, PositionType type) {
        return member.getPositions().stream()
                .filter(position -> position.getType() == type)
                .map(MemberPosition::getName)
                .toList();
    }

    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member loadEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER));
    }
}
