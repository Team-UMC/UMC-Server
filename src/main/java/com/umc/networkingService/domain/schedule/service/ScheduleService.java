package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryLists;
import java.util.UUID;

public interface ScheduleService {
    public ScheduleInfoSummariesInCalendar getCalendarByMonth(Long month);
    public ScheduleId createSchedule(CreateSchedule request);
    public ScheduleId updateSchedule(UUID scheduleId, UpdateSchedule request);

    public ScheduleId deleteSchedule(UUID scheduleId);

    public ScheduleInfoSummaryLists getScheduleLists(Long month);

}
