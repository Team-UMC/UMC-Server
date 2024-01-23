package com.umc.networkingService.domain.todayILearned.controller;

import com.umc.networkingService.domain.todayILearned.service.TodayILearnedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Today I Learned API", description = "Today I Learned 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/todayILearned")
public class TodayILearnedController {

    private final TodayILearnedService todayILearnedService;
}
