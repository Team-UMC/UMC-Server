package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPoint;
import com.umc.networkingService.domain.member.entity.PointType;
import com.umc.networkingService.domain.member.repository.MemberPointRepository;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberPointService;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.university.converter.UniversityConverter;
import com.umc.networkingService.domain.university.dto.request.UniversityRequest;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
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


    private final S3FileComponent s3FileComponent;

    @Override
    public University findUniversityByName(String universityName) {
        return universityRepository.findByName(universityName)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_UNIVERSITY));
    }

    @Transactional(readOnly = true)     //전체 대학교 조회
    public UniversityResponse.JoinUniversities joinUniversityList(){

        List<University> universityList = universityRepository.findAllByOrderByNameAsc(); //이름 순 정렬
        return UniversityResponse.JoinUniversities.builder()
                .joinUniversities(UniversityConverter.toJoinUniversityList(universityList))
                .build();
    }

    @Transactional(readOnly = true)     //대학교 별 세부 정보 조회
    public UniversityResponse.joinUniversityDetail joinUniversityDetail(Member member){
        UniversityResponse.joinUniversityDetail universityDetail
                = UniversityConverter.toJoinUniversityDetail(member.getUniversity());

        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc(); //랭킹 순 정렬

        return UniversityResponse.joinUniversityDetail.setUniversityRank(
                universityDetail,universityRankList.indexOf(member.getUniversity())+1
        );

    }

    @Transactional(readOnly = true)     //전체 대학교 랭킹 조회
    public UniversityResponse.JoinUniversityRanks joinUniversityRankingList(Member member){
        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc();
        return UniversityResponse.JoinUniversityRanks.builder()
                .joinUniversityRanks(UniversityConverter.toJoinUniversityRankList(universityRankList))
                .build();
    }

    @Transactional(readOnly = true)     //우리 학교 기여도 랭킹 조회
    public UniversityResponse.JoinContributionRanks joinContributionRankingList(Member member){

        List<Member> contributionRankList = memberService.findContributionRankings(member);
        return UniversityResponse.JoinContributionRanks.builder()
                        .joinContributionRanks(
                                UniversityConverter.toJoinContributionRankList(contributionRankList)
                        ).build();
    }

    @Transactional(readOnly = true)    //우리 대학교 마스코트 조회
    public UniversityResponse.joinUniversityMascot joinUniversityMascot(Member member){

        UniversityResponse.joinUniversityMascot universityMascot
                = UniversityConverter.toJoinUniversityMascot(member.getUniversity());

        //지부 찾기
        Branch branch = branchUniversityService.findBranchByUniversity(member.getUniversity());

        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc(); //랭킹 순 정렬

        return UniversityResponse.joinUniversityMascot.setRankAndBranch(
                universityMascot
                ,universityRankList.indexOf(member.getUniversity())+1
                ,branch
        );
    }

    @Transactional    //우리 대학교 마스코트 먹이주기  todo: 마스코드 레벨업, 마스코트 변경
    public void feedUniversityMascot(Member member, PointType pointType){

        if(member.getRemainPoint() < pointType.getPoint()){
            throw new RestApiException(ErrorCode.NOT_ENOUGH_POINT);
        }
        
        //포인트 차감
        member.usePoint(pointType.getPoint());
        //학교 포인트 증가
        member.getUniversity().increasePoint(pointType.getPoint());

        //포인트 히스토리 추가
        memberPointService.saveEntity(
                MemberPoint.builder()
                        .member(member)
                        .pointType(pointType)
                        .university(member.getUniversity())
                        .build()
        );


    }

    @Transactional    //학교 생성
    public UUID createUniversity(UniversityRequest.createUniversity request){
        if(universityRepository.findByName(request.getUniversityName()).isPresent()){
            throw new RestApiException(ErrorCode.DUPLICATE_UNIVERSITY_NAME);
        }
        return universityRepository.save(University.builder()
                .name(request.getUniversityName())
                .universityLogo(uploadImage("university",request.getUniversityLogo()))
                .semesterLogo(uploadImage("semester",request.getSemesterLogo()))
                .build()).getId();
    }

    @Transactional   //학교 삭제
    public UUID deleteUniversity(UUID universityId){
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_UNIVERSITY));
        university.delete();
        return universityId;
    }

    @Transactional    //학교 정보 수정
    public UUID patchUniversity(UniversityRequest.patchUniversity request){

        University university;
        university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_UNIVERSITY));

        university.updateUniversity(
                request.getUniversityName()
                ,uploadImage("university",request.getUniversityLogo())
                ,uploadImage("semester",request.getSemesterLogo()));
        universityRepository.save(university);
        return university.getId();
    }

    //s3에 이미지 업로드
    public String uploadImage(String category,MultipartFile imageFile){
        if(imageFile==null ||imageFile.isEmpty()){
            return "";
        }
        return s3FileComponent.uploadFile(category, imageFile);
    }


}
