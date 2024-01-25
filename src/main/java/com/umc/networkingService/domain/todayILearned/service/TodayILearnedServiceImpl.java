package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfos;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.mapper.TodayILearnedMapper;
import com.umc.networkingService.domain.todayILearned.repository.TodayILearnedRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TodayILearnedServiceImpl implements TodayILearnedService {

    private final TodayILearnedFileService todayILearnedFileService;
    private final TodayILearnedRepository todayILearnedRepository;
    private final TodayILearnedMapper todayILearnedMapper;

    @Override
    public TodayILearnedId createTodayILearned(Member member, TodayILearnedCreate request, List<MultipartFile> files) {

        TodayILearned todayILearned = todayILearnedRepository.save(todayILearnedMapper.toTodayILearned(member, request));

        System.out.println(todayILearned);
        // 파일이 있으면 파일 업로드
        if (files != null) {
            todayILearnedFileService.uploadTodayILearnedFiles(todayILearned, files);
        }

        return todayILearnedMapper.UUIDtoTodayILearnedId(todayILearned.getId());
    }

    @Override
    public TodayILearnedInfos getTodayILearnedInfos(Member member) {

        return todayILearnedMapper.toTodayILearnedInfos(
                todayILearnedRepository.findTodayILearnedByWriterAndCreateDate(member,
                        LocalDate.now()).stream()
                        .map(todayILearned -> todayILearnedMapper.toTodayILearnedInfo(todayILearned))
                        .toList());
    }
}
