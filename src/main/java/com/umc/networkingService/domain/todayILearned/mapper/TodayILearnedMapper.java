package com.umc.networkingService.domain.todayILearned.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
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

    public TodayILearnedId UUIDtoTodayILearnedId(UUID todayILearnedId) {
        return TodayILearnedId.builder()
                .todayILearnedId(todayILearnedId)
                .build();
    }

}
