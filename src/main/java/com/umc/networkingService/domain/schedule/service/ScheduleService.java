package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.*;

import java.time.LocalDate;
import java.util.UUID;

public interface ScheduleService {
    ScheduleInfoSummariesInCalendar getScheduleByMonth(Member member, LocalDate date);
    ScheduleInfos getScheduleByMonthToWeb(Member member, LocalDate date);
    ScheduleId createSchedule(Member member, CreateSchedule request);
    ScheduleId updateSchedule(Member member, UUID scheduleId, UpdateSchedule request);
    ScheduleId deleteSchedule(Member member, UUID scheduleId);
    ScheduleInfoSummaryLists getScheduleLists(Member member, LocalDate date);
    ScheduleInfo getScheduleDetail(Member member, UUID scheduleId);
    boolean existsByHostType(HostType hostType);
}