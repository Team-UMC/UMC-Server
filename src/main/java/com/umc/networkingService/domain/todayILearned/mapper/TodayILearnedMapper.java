package com.umc.networkingService.domain.todayILearned.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedUpdate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfo;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfos;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TodayILearnedMapper {

    public TodayILearned toTodayILearned(Member member, TodayILearnedCreate request) {
        return TodayILearned.builder()
                .writer(member)
                .title(request.getTitle())
                .subtitle(request.getSubTitle())
                .content(request.getContent())
                .part(request.getPart())
                .build();
    }

    public TodayILearnedId toTodayILearnedId(UUID todayILearnedId) {
        return TodayILearnedId.builder()
                .todayILearnedId(todayILearnedId)
                .build();
    }

    public TodayILearnedInfo toTodayILearnedInfo(TodayILearned todayILearned) {
        return TodayILearnedInfo.builder()
                .todayILearnedId(todayILearned.getId())
                .title(todayILearned.getTitle())
                .subTitle(todayILearned.getSubtitle())
                .part(todayILearned.getPart())
                .build();
    }

    public TodayILearnedInfos toTodayILearnedInfos(List<TodayILearnedInfo> todayILearnedInfos) {
        return TodayILearnedInfos.builder()
                .todayILearnedInfos(todayILearnedInfos)
                .build();
    }


}
