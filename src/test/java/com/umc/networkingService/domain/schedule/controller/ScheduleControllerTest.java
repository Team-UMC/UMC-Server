package com.umc.networkingService.domain.schedule.controller;

import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.mapper.ScheduleMapper;
import com.umc.networkingService.domain.schedule.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("Schedule 컨트롤러의")
public class ScheduleControllerTest extends ScheduleControllerTestConfig {

    @Autowired private ScheduleMapper scheduleMapper;

    @MockBean private ScheduleService scheduleService;

    @DisplayName("캘린더 조회 API 테스트")
    @Test
    public void getScheduleTEst() throws Exception {
        // given
        Long month = 2L;

        
    }
}
