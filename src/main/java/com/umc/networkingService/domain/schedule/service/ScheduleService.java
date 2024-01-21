package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfo;
import java.util.List;

public interface ScheduleService {
    public List<ScheduleInfo> getSchedulesByMonth(Long month);
}
