package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedUpdate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfos;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedWebInfos;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
import com.umc.networkingService.domain.todayILearned.mapper.TodayILearnedMapper;
import com.umc.networkingService.domain.todayILearned.repository.TodayILearnedRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.TodayILearnedErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodayILearnedServiceImpl implements TodayILearnedService {

    private final TodayILearnedFileService todayILearnedFileService;
    private final TodayILearnedRepository todayILearnedRepository;
    private final TodayILearnedMapper todayILearnedMapper;

    private final MemberService memberService;

    @Override
    @Transactional
    public TodayILearnedResponse.TodayILearnedCreate createTodayILearned(Member loginMember, List<MultipartFile> files, TodayILearnedCreate request) {

        Member member = memberService.loadEntity(loginMember.getId());

        TodayILearned todayILearned = todayILearnedRepository.save(todayILearnedMapper.toTodayILearned(member, request));

        // 파일이 있으면 파일 업로드
        if (files != null) {
            todayILearnedFileService.uploadTodayILearnedFiles(todayILearned, files);
        }

        return new TodayILearnedResponse.TodayILearnedCreate(
                todayILearned.getId(),
                validatePointAcquired(todayILearned, member)
        );
    }

    @Override
    public TodayILearnedInfos getTodayILearnedInfos(Member member, String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(stringDate,formatter);

        return todayILearnedMapper.toTodayILearnedInfos(
                todayILearnedRepository.findTodayILearnedByWriterAndCreateDate(member, date)
                        .stream()
                        .map(todayILearnedMapper::toTodayILearnedInfo)
                        .toList());
    }

    @Override
    public TodayILearnedWebInfos getTodayILearnedWebInfos(Member member, String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(stringDate,formatter);

        return new TodayILearnedWebInfos(
                todayILearnedRepository.findTodayILearnedByWriterAndCreateDate(member, date)
                        .stream()
                        .map(todayILearnedMapper::toTodayILearnedWebInfo)
                        .toList());
    }

    @Override
    @Transactional
    public TodayILearnedId updateTodayILearned(Member member, UUID todayILearnedId, List<MultipartFile> files,
                                               TodayILearnedUpdate request) {
        TodayILearned todayILearned = loadEntity(todayILearnedId);

        // 만약 삭제하려는 멤버와 TIL 작성자가 일치하지 않을 경우 에러 반환
        validateMember(todayILearned, member);

        todayILearned.updateTodayILearned(request);
        todayILearnedFileService.updateTodayILearnedFiles(todayILearned, files);

        return todayILearnedMapper.toTodayILearnedId(todayILearned.getId());
    }

    @Override
    @Transactional
    public TodayILearnedId deleteTodayILearned(Member member, UUID todayILearnedId) {
        TodayILearned todayILearned = loadEntity(todayILearnedId);

        // 만약 삭제하려는 멤버와 TIL 작성자가 일치하지 않을 경우 에러 반환
        validateMember(todayILearned, member);

        todayILearnedFileService.deleteTodayILearnedFiles(todayILearned);
        todayILearned.delete();

        return todayILearnedMapper.toTodayILearnedId(todayILearned.getId());
    }

    @Override
    public TodayILearnedResponse.TodayILearnedDetail getTodayILearnedDetail(
            Member member, UUID todayILearnedId) {

        TodayILearned todayILearned = loadEntity(todayILearnedId);

        validateMember(todayILearned, member);

        return todayILearnedMapper.toTodayILearnedDetail(
                todayILearned,
                todayILearnedFileService.findTodayILearnedFiles(todayILearned).stream()
                        .map(TodayILearnedFile::getUrl)
                        .toList());
    }

    private void validateMember(TodayILearned todayILearned, Member member) {
        if (!todayILearned.getWriter().getId().equals(member.getId()))
            throw new RestApiException(TodayILearnedErrorCode.NO_PERMISSION_EMPTY_TODAYILERARNED_MEMBER);
    }

    // 포인트 획득 가능 여부 확인 함수
    private boolean validatePointAcquired(TodayILearned todayILearned, Member member) {
        List<TodayILearned> todayILearneds = todayILearnedRepository.findAllByWriterAndCreatedAtBetween(member,
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX)
        );

        todayILearneds.sort(Comparator.comparing(TodayILearned::getCreatedAt));

        int index = todayILearneds.indexOf(todayILearned);

        if (index == -1) throw new RestApiException(TodayILearnedErrorCode.EMPTY_TODAYILERARNED);

        if (index < 2) {
            member.addRemainPoint(1L);
            return true;
        }
        return false;
    }

    @Override
    public TodayILearned loadEntity(UUID todayILearnedId) {
        return todayILearnedRepository.findById(todayILearnedId).orElseThrow(() -> new RestApiException(
                TodayILearnedErrorCode.EMPTY_TODAYILERARNED));
    }

}
