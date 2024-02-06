package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
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
    private final BranchUniversityService branchUniversityService;

    private final S3FileComponent s3FileComponent;
    private final GithubMemberClient githubMemberClient;

    // 나의 프로필 업데이트 함수
    @Override
    @Transactional
    public MemberIdResponse updateMyProfile(Member loginMember, MultipartFile profileImage, MemberUpdateMyProfileRequest request) {
        Member member = loadEntity(loginMember.getId());

        // 사진이 존재한다면 업로드 후 url 저장
        String profileUrl = uploadProfileImage(profileImage);
        member.updateMemberInfo(request, profileUrl);

        return new MemberIdResponse(memberRepository.save(loginMember).getId());
    }

    // 프로필 수정 함수(운영진용)
    @Override
    @Transactional
    public MemberIdResponse updateProfile(Member member, UUID memberId, MemberUpdateProfileRequest request) {

        // 수정할 유저 탐색
        Member updateMember = loadEntity(memberId);

        // 수정 권한 검증
        checkUpdateAuthority(member, updateMember, request.getCenterPositions());

        // 직책 및 기수별 파트 정보 수정
        updatePositionAndSemesterPart(updateMember, request);

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
    public MemberAuthenticateGithubResponse authenticateGithub(Member member, String nickname) {

        Member loginMember = loadEntity(member.getId());

////        String gitNickname = githubMemberClient.getGithubNickname(code);
//
//        if (gitNickname == null || gitNickname.isBlank())
//            throw new RestApiException(ErrorCode.FAILED_GITHUB_AUTHENTICATION);

        if (memberRepository.existsByGitNickname(nickname))
            throw new RestApiException(ErrorCode.DUPLICATED_GIT_NICKNAME);
        loginMember.authenticateGithub(nickname);

        return new MemberAuthenticateGithubResponse(loginMember.getGitNickname());
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

        List<MemberInquiryPointsResponse.UsedHistory> usedHistories = getUsedHistories(member);

        return memberMapper.toInquiryPointsResponse(loginMember.getRemainPoint(), usedHistories);
    }

    // 멤버 검색 함수(운영진용)
    @Override
    public MemberSearchInfosResponse searchMemberInfo(Member loginMember, String keyword) {
        Member member = loadEntity(loginMember.getId());

        // keyword 양식 검증
        String[] nicknameAndName = validateKeyword(keyword);

        // 해당 유저가 본인보다 상위 운영진인 경우 검색 대상에서 제외
        List<MemberSearchInfosResponse.MemberInfo> memberInfos = getMemberInfos(nicknameAndName, member);

        return new MemberSearchInfosResponse(memberInfos);
    }

    @Override
    @Transactional
    public void updateMemberActiveTime(UUID memberId) {
        Member loginMember = loadEntity(memberId);

        loginMember.updateLastActiveTime(LocalDateTime.now());
    }

    private String uploadProfileImage(MultipartFile profileImage) {
        if (profileImage != null) {
            return s3FileComponent.uploadFile("member", profileImage);
        }
        return null;
    }

    private void checkUpdateAuthority(Member member, Member updateMember, List<String> centerPositions) {
        if (updateMember.getRole().getPriority() <= member.getRole().getPriority()
                && !member.getId().equals(updateMember.getId())) {
            // 본인보다 높거나 같은 직책의 운영진의 정보를 수정하려 할 경우
            throw new RestApiException(ErrorCode.UNAUTHORIZED_UPDATE_MEMBER);
        }
        if (member.getRole().getPriority() > 2 && !centerPositions.isEmpty()) {
            // 학교, 지부 운영진이 중앙 직책을 수정하려는 경우
            throw new RestApiException(ErrorCode.UNAUTHORIZED_UPDATE_CENTER_POSITION);
        }
    }

    private void updatePositionAndSemesterPart(Member updateMember, MemberUpdateProfileRequest request) {
        // 직책 수정
        memberPositionService.saveMemberPositionInfos(updateMember, request.getCampusPositions(), request.getCenterPositions());

        // 직책에 따른 Role 수정
        Role newRole = findMemberRole(updateMember.getPositions());
        updateMember.updateRole(newRole);

        // 특정 기수의 파트 변경
        semesterPartService.saveSemesterPartInfos(updateMember, request.getSemesterParts());

        // 기수 변경에 의해 소속 지부 변경
        Branch newBranch = branchUniversityService.findBranchByUniversityAndSemester(
                updateMember.getUniversity(), updateMember.getRecentSemester());
        updateMember.updateBranch(newBranch);
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

    // 기여도 목록 조회 함수
    private List<MemberInquiryPointsResponse.UsedHistory> getUsedHistories(Member member) {
        Page<MemberPoint> usedPointsPage = memberPointRepository.
                findAllByMemberOrderByCreatedAtDesc(member, PageRequest.of(0, 2));

        return usedPointsPage.stream()
                .map(MemberPoint::getPointType)
                .map(memberMapper::toUsedHistory)
                .toList();
    }

    // 검색어로 멤버 정보 조회 함수
    private List<MemberSearchInfosResponse.MemberInfo> getMemberInfos(String[] nicknameAndName, Member member) {
        List<Member> searchedMembers = memberRepository.findAllByNicknameAndName(nicknameAndName[0], nicknameAndName[1]).stream()
                .filter(searchedMember -> searchedMember.getRole().getPriority() > member.getRole().getPriority())
                .toList();

        return searchedMembers.stream()
                .map(searchedMember -> memberMapper.toSearchMembersResponse(
                        searchedMember,
                        getPositionNamesByType(searchedMember, PositionType.CAMPUS),
                        getPositionNamesByType(searchedMember, PositionType.CENTER)
                )).toList();
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
    @Override
    public List<String> getPositionNamesByType(Member member, PositionType type) {
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

    @Override
    public List<Member> findContributionRankings(Member member) {
        return memberRepository.findAllByUniversityOrderByContributionPointDesc(member.getUniversity());
    }

    @Override
    public University findUniversityByMember(Member member) {
        return member.getUniversity();
    }

    @Override
    @Transactional
    public Member usePoint(Member member, PointType pointType) {
        Member loginMember = loadEntity(member.getId());

        loginMember.usePoint(pointType.getPoint());

        return memberRepository.save(loginMember);
    }
}
