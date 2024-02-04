package com.umc.networkingService.domain.todayILearned.mapper;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
import org.springframework.stereotype.Component;

@Component
public class TodayILearnedFileMapper {
    public TodayILearnedFile toTodayILearnedFile(TodayILearned todayILearned, String url) {
        return TodayILearnedFile.builder()
                .todayILearned(todayILearned)
                .url(url)
                .build();
    }
}
