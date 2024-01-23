package com.umc.networkingService.domain.university.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.university.converter.UniversityConverter;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    @Override
    public University findUniversityByName(String universityName) {
        return universityRepository.findByName(universityName)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_UNIVERSITY));
    }

    @Transactional(readOnly = true)     //전체 대학교 조회
    public List<UniversityResponse.joinUniversity> joinUniversityList(){
        List<University> universityList = universityRepository.findAll();
        return UniversityConverter.toJoinUniversityList(universityList);
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
    public List<UniversityResponse.joinUniversityRank> joinUniversityRankingList(Member member){
        List<University> universityRankList = universityRepository.findAllByOrderByTotalPointDesc();
        return UniversityConverter.toJoinUniversityRankList(universityRankList);
    }



    //유효한 대학 확인
    public boolean isUniversityValid(UUID universityId) {
        return universityRepository.existsById(universityId);
    }
}
