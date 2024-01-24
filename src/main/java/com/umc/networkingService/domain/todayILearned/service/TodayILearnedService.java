package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TodayILearnedService {
    TodayILearnedId createTodayILearned(Member member, TodayILearnedCreate request, List<MultipartFile> files);
}
