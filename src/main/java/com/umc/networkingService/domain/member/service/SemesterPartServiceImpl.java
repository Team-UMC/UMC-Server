package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.SemesterPartErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SemesterPartServiceImpl implements SemesterPartService {

    private final SemesterPartRepository semesterPartRepository;

    private final MemberMapper memberMapper;

    // 기수별 파트 정보 저장 함수
    @Override
    public void saveSemesterPartInfos(Member member, List<SemesterPartInfo> semesterPartInfos) {
        // 기존 기수별 파트가 있다면 삭제
        member.getSemesterParts().forEach(SemesterPart::delete);

        List<SemesterPart> semesterParts = saveSemesterParts(member, semesterPartInfos);

        member.updateSemesterParts(semesterParts);
    }

    private List<SemesterPart> saveSemesterParts(Member member, List<SemesterPartInfo> semesterPartInfos) {
        List<SemesterPart> semesterParts = semesterPartInfos.stream()
                .map(semesterPart -> memberMapper.toSemesterPart(member, semesterPart))
                .toList();

        // 디비에 저장
        return semesterPartRepository.saveAll(semesterParts);

    }

    // 엔티티 로드 함수
    @Override
    public SemesterPart loadEntity(UUID id) {
        return semesterPartRepository.findById(id)
                .orElseThrow(() -> new RestApiException(SemesterPartErrorCode.EMPTY_SEMESTER_PART));
    }
}
