package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoList;

public interface ScheduleService {
    public ScheduleInfoList getCalendarByMonth(Long month);
}
