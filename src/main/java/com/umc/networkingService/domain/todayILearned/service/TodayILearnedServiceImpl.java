package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.todayILearned.mapper.TodayILearnedMapper;
import com.umc.networkingService.domain.todayILearned.repository.TodayILearnedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodayILearnedServiceImpl implements TodayILearnedService {
    private final TodayILearnedRepository todayILearnedRepository;
    private final TodayILearnedMapper todayILearnedMapper;
}
