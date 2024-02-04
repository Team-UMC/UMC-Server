package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedUpdate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfos;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.mapper.TodayILearnedMapper;
import com.umc.networkingService.domain.todayILearned.repository.TodayILearnedRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TodayILearnedServiceImpl implements TodayILearnedService {

    private final TodayILearnedFileService todayILearnedFileService;
    private final TodayILearnedRepository todayILearnedRepository;
    private final TodayILearnedMapper todayILearnedMapper;

    @Override
    public TodayILearnedId createTodayILearned(Member member, List<MultipartFile> files, TodayILearnedCreate request) {

        TodayILearned todayILearned = todayILearnedRepository.save(todayILearnedMapper.toTodayILearned(member, request));

        // 파일이 있으면 파일 업로드
        if (files != null) {
            todayILearnedFileService.uploadTodayILearnedFiles(todayILearned, files);
        }

        return todayILearnedMapper.toTodayILearnedId(todayILearned.getId());
    }

    @Override
    public TodayILearnedInfos getTodayILearnedInfos(Member member) {

        return todayILearnedMapper.toTodayILearnedInfos(
                todayILearnedRepository.findTodayILearnedByWriterAndCreateDate(member,
                        LocalDate.now()).stream()
                        .map(todayILearned -> todayILearnedMapper.toTodayILearnedInfo(todayILearned))
                        .toList());
    }

    @Override
    @Transactional
    public TodayILearnedId updateTodayILearned(Member member, UUID todayILearnedId, List<MultipartFile> files,
                                               TodayILearnedUpdate request) {
        TodayILearned todayILearned = todayILearnedRepository.findById(todayILearnedId).orElseThrow(() -> new RestApiException(
                ErrorCode.EMPTY_TODAYILERARNED));

        // 만약 삭제하려는 멤버와 TIL 작성자가 일치하지 않을 경우 에러 반환
        validateMember(todayILearned, member);
        todayILearned.updateTodayILearned(request);


        return todayILearnedMapper.toTodayILearnedId(todayILearned.getId());
    }

    @Override
    @Transactional
    public TodayILearnedId deleteTodayILearned(Member member, UUID todayILearnedId) {
        TodayILearned todayILearned = todayILearnedRepository.findById(todayILearnedId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODAYILERARNED));

        // 만약 삭제하려는 멤버와 TIL 작성자가 일치하지 않을 경우 에러 반환
        validateMember(todayILearned, member);
        todayILearned.delete();

        return todayILearnedMapper.toTodayILearnedId(todayILearned.getId());
    }

    private void validateMember(TodayILearned todayILearned, Member member) {
        if (!todayILearned.getWriter().getId().equals(member.getId()))
            throw new RestApiException(ErrorCode.NO_PERMISSION_EMPTY_TODAYILERARNED_MEMBER);
    }
}
