package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedUpdate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfos;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface TodayILearnedService {
    TodayILearnedResponse.TodayILearnedCreate createTodayILearned(Member member, List<MultipartFile> files, TodayILearnedCreate request);
    TodayILearnedInfos getTodayILearnedInfos(Member member);
    TodayILearnedId updateTodayILearned(Member member, UUID todayILearnedId, List<MultipartFile> files, TodayILearnedUpdate request);
    TodayILearnedId deleteTodayILearned(Member member, UUID todayILearnedId);
}
