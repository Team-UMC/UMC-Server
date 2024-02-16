package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleDetail;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryLists;

import java.time.LocalDate;
import java.util.UUID;

public interface ScheduleService {
    ScheduleInfoSummariesInCalendar getCalendarByMonth(LocalDate date);
    ScheduleId createSchedule(Member member, CreateSchedule request);
    ScheduleId updateSchedule(Member member, UUID scheduleId, UpdateSchedule request);

    ScheduleId deleteSchedule(Member member, UUID scheduleId);

    ScheduleInfoSummaryLists getScheduleLists(LocalDate date);

    ScheduleDetail getScheduleDetail(Member member, UUID scheduleId);

}