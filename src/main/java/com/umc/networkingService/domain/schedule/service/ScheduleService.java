package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoList;
import java.util.List;

public interface ScheduleService {
    public ScheduleInfoList getSchedulesByMonth(Long month);
}
