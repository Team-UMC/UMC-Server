package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.mascot.service.MascotService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPoint;
import com.umc.networkingService.domain.member.entity.PointType;
import com.umc.networkingService.domain.member.service.MemberPointService;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.university.mapper.UniversityMapper;
import com.umc.networkingService.domain.university.dto.request.UniversityRequest;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.MemberErrorCode;
import com.umc.networkingService.global.utils.S3FileComponent;
import com.umc.networkingService.global.common.exception.code.UniversityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    private final MemberService memberService;
    private final BranchUniversityService branchUniversityService;
    private final MemberPointService memberPointService;
    private final MascotService mascotService;

    private final S3FileComponent s3FileComponent;

    @Override
    public University findUniversityByName(String universityName) {
        return universityRepository.findByName(universityName)
                .orElseThrow(() -> new RestApiException(UniversityErrorCode.EMPTY_UNIVERSITY));
    }

    @Transactional(readOnly = true)     //전체 대학교 조회
    public UniversityResponse.JoinUniversities joinUniversityList() {

        List<University> universityList = universityRepository.findAllByOrderByNameAsc(); //이름 순 정렬
        return UniversityResponse.JoinUniversities.builder()
                .joinUniversities(UniversityMapper.toJoinUniversityList(universityList))
                .build();
    }

    @Transactional(readOnly = true)     //우리 학교 정보 조회
    public UniversityResponse.joinUniversityDetail joinUniversityDetail(Member member) {
        Member memberEntity = memberService.findByMemberId(member.getId());

        UniversityResponse.joinUniversityDetail universityDetail
                = UniversityMapper.toJoinUniversityDetail(
                memberEntity.getUniversity()
        );

        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc(); //랭킹 순 정렬

        return UniversityResponse.joinUniversityDetail.setUniversityRank(
                universityDetail
                , handleTiedUniversityRanks( //우리 학교 순위
                        UniversityResponse.JoinUniversityRanks.builder()
                                .joinUniversityRanks(UniversityMapper.toJoinUniversityRankList(universityRankList))
                                .build()
                        ,memberEntity.getUniversity()
                )
        );

    }

    @Transactional(readOnly = true)     //전체 대학교 랭킹 조회
    public UniversityResponse.JoinUniversityRanks joinUniversityRankingList() {
        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc();

        return handleTiedUniversityRanks(UniversityResponse.JoinUniversityRanks.builder()
                .joinUniversityRanks(UniversityMapper.toJoinUniversityRankList(universityRankList))
                .build());
    }

    @Transactional(readOnly = true)     //우리 학교 기여도 랭킹 조회
    public UniversityResponse.JoinContributionRanks joinContributionRankingList(Member member) {
        Member memberEntity = memberService.findByMemberId(member.getId());
        List<Member> contributionRankList = memberService.findContributionRankings(memberEntity);
        UniversityResponse.JoinContributionRanks joinContributionRanks = UniversityResponse.JoinContributionRanks.builder()
                .joinContributionRanks(
                        UniversityMapper.toJoinContributionRankList(contributionRankList)
                ).build();

        return handleTiedContributionRanks(joinContributionRanks);
    }

    @Transactional(readOnly = true)    //우리 대학교 마스코트 조회
    public UniversityResponse.joinUniversityMascot joinUniversityMascot(Member member) {
        Member memberEntity = memberService.findByMemberId(member.getId());

        UniversityResponse.joinUniversityMascot universityMascot
                = UniversityMapper.toJoinUniversityMascot(
                memberEntity.getUniversity()
                ,memberEntity.getUniversity().getMascot()
        );

        //지부 찾기
        Branch branch = branchUniversityService.findBranchByUniversity(memberEntity.getUniversity());

        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc(); //랭킹 순 정렬

        UniversityResponse.JoinUniversityRanks joinUniversityRanks = UniversityResponse.JoinUniversityRanks.builder()
                .joinUniversityRanks(UniversityMapper.toJoinUniversityRankList(universityRankList))
                .build();

        return UniversityResponse.joinUniversityMascot.setRankAndBranch(
                universityMascot,
                handleTiedUniversityRanks(joinUniversityRanks, memberEntity.getUniversity()),
                branch
        );
    }

    @Transactional    //우리 대학교 마스코트 먹이주기
    public UniversityResponse.JoinUniversityMascotFeed feedUniversityMascot(Member member, PointType pointType) {
        Member memberEntity = memberService.findByMemberId(member.getId());

        if (memberEntity.getRemainPoint() < pointType.getPoint()) {
            throw new RestApiException(MemberErrorCode.NOT_ENOUGH_POINT);
        }

        //포인트 차감
        memberService.usePoint(memberEntity, pointType);

        //학교 포인트 증가
        University university = memberEntity.getUniversity();
        university.increasePoint(pointType.getPoint());

        //포인트 히스토리 추가
        memberPointService.saveEntity(
                MemberPoint.builder()
                        .member(memberEntity)
                        .pointType(pointType)
                        .university(university)
                        .build()
        );

        //마스코트 설정하기
        Integer currentLevel = (int)(university.getTotalPoint() / 200 + 1);
        if(!currentLevel.equals(university.getCurrentLevel())){
            university.setLevel((int) (currentLevel));
            if(university.getMascot().getEndLevel()<currentLevel){
                university.setMascot(mascotService.getMascotByStartLevel(currentLevel, university.getMascot().getType()));
            }
        }
        /*
        * 1. 현재 대학교 포인트 200 단위로 마스코트 변경됨,현재 마스코트 레벨 찾기, 현재 레벨과 다르면 마스코트 변경
        * 2. 레벨*10하면 마스코트의 끝 레벨 나옴, 이걸 이용해서 레벨에 맞는 마스코트 재설정, 마스코트 레벨 재설정
        * */
        universityRepository.save(university);
        return UniversityResponse.JoinUniversityMascotFeed.builder()
                .universityTotalpoint(university.getTotalPoint())
                .memberRemainPoint(memberEntity.getRemainPoint())
                .build();
    }

    @Transactional    //학교 생성
    public UniversityResponse.UniversityId createUniversity(UniversityRequest.createUniversity request) {
        if (universityRepository.findByName(request.getUniversityName()).isPresent()) {
            throw new RestApiException(UniversityErrorCode.DUPLICATE_UNIVERSITY_NAME);
        }
        return UniversityResponse.UniversityId.builder()
                        .universityId(
                                universityRepository.save(University.builder()
                                        .name(request.getUniversityName())
                                        .universityLogo(uploadImage("university", request.getUniversityLogo()))
                                        .semesterLogo(uploadImage("semester", request.getSemesterLogo()))
                                        .build()).getId()
                        ).build();

    }

    @Transactional   //학교 삭제
    public UniversityResponse.UniversityId deleteUniversity(UUID universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RestApiException(UniversityErrorCode.EMPTY_UNIVERSITY));
        university.delete();
        return UniversityResponse.UniversityId.builder()
                .universityId(universityId)
                .build();
    }

    @Transactional    //학교 정보 수정
    public UniversityResponse.UniversityId patchUniversity(UniversityRequest.patchUniversity request) {

        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new RestApiException(UniversityErrorCode.EMPTY_UNIVERSITY));

        university.updateUniversity(
                request.getUniversityName()
                , uploadImage("university", request.getUniversityLogo())
                , uploadImage("semester", request.getSemesterLogo()));

        return UniversityResponse.UniversityId.builder()
                        .universityId(university.getId())
                        .build();
    }

    //s3에 이미지 업로드
    public String uploadImage(String category, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return "";
        }
        return s3FileComponent.uploadFile(category, imageFile);
    }

    //동점자 처리 (기여도) (동점차는 동일한 순위 부여 후 랭킹 건너뛰기)
    private UniversityResponse.JoinContributionRanks handleTiedContributionRanks(UniversityResponse.JoinContributionRanks ranks) {
        int currentRank = 1;
        Long currentScore = ranks.getJoinContributionRanks().get(0).getUsedPoint();

        for (int indx = 0; indx < ranks.getJoinContributionRanks().size(); indx++) {
            if (ranks.getJoinContributionRanks().get(indx).getUsedPoint().equals(currentScore)) {
                UniversityResponse.JoinContributionRank.setRank(ranks.getJoinContributionRanks().get(indx), currentRank);
                continue;
            }
            currentRank= ranks.getJoinContributionRanks().get(indx).getRank();
            currentScore = ranks.getJoinContributionRanks().get(indx).getUsedPoint();
        }
        return ranks;
    }

    //동점학교 처리 (학교 랭킹) (동점차는 동일한 순위 부여 후 랭킹 건너뛰기)
    private UniversityResponse.JoinUniversityRanks handleTiedUniversityRanks(UniversityResponse.JoinUniversityRanks ranks) {
        int currentRank = 1;
        Long currentScore = ranks.getJoinUniversityRanks().get(0).getUniversityPoint();

        for (int indx = 0; indx < ranks.getJoinUniversityRanks().size(); indx++) {
            if (ranks.getJoinUniversityRanks().get(indx).getUniversityPoint().equals(currentScore)) {
                UniversityResponse.JoinUniversityRank.setRank(ranks.getJoinUniversityRanks().get(indx), currentRank);
                continue;
            }
            currentRank= ranks.getJoinUniversityRanks().get(indx).getUniversityRank();
            currentScore = ranks.getJoinUniversityRanks().get(indx).getUniversityPoint();
        }
        return ranks;
    }

    //동점학교 처리, (해당 학교 랭킹 반환) (동점차는 동일한 순위 부여 후 랭킹 건너뛰기)
    private  Integer handleTiedUniversityRanks(UniversityResponse.JoinUniversityRanks ranks, University university) {
        int currentRank = 1;
        Long currentScore = ranks.getJoinUniversityRanks().get(0).getUniversityPoint();

        for (int indx = 0; indx < ranks.getJoinUniversityRanks().size(); indx++) {
            if (ranks.getJoinUniversityRanks().get(indx).getUniversityPoint().equals(currentScore)) {
                UniversityResponse.JoinUniversityRank.setRank(ranks.getJoinUniversityRanks().get(indx), currentRank);

            }else{
                currentRank= ranks.getJoinUniversityRanks().get(indx).getUniversityRank();
                currentScore = ranks.getJoinUniversityRanks().get(indx).getUniversityPoint();
            }
            if (ranks.getJoinUniversityRanks().get(indx).getUniversityName().equals(university.getName())) {
                return ranks.getJoinUniversityRanks().get(indx).getUniversityRank();
            }
        }
        return 0;
    }
}
