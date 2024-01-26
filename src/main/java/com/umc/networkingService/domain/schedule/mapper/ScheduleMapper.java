package com.umc.networkingService.domain.schedule.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleDetail;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummary;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryLists;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleDetail toScheduleDetail(Schedule schedule) {
        return ScheduleDetail.builder()
                .scheduleId(schedule.getId())
                .writer(schedule.getWriter())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .placeSetting(schedule.getPlaceSetting())
                .hostType(schedule.getHostType())
                .build();
    }

    public ScheduleInfoSummaryInCalendar toScheduleInfoSummaryInCalendar(Schedule schedule) {
        return ScheduleInfoSummaryInCalendar.builder()
                .scheduleId(schedule.getId())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .hostType(schedule.getHostType().toString())
                .build();
    }

    public ScheduleInfoSummariesInCalendar toScheduleInfoSummariesInCalendar(List<ScheduleInfoSummaryInCalendar> schedules) {
        return ScheduleInfoSummariesInCalendar.builder()
                .schedules(schedules)
                .build();
    }

    public ScheduleInfoSummary toScheduleInfoSummary(Schedule schedule) {
        return ScheduleInfoSummary.builder()
                .scheduleId(schedule.getId())
                .startDateTime(schedule.getStartDateTime())
                .endDatetime(schedule.getEndDateTime())
                .title(schedule.getTitle())
                .hostType(schedule.getHostType())
                .build();
    }

    public ScheduleInfoSummaryLists toScheduleInfoSummaries(List<ScheduleInfoSummary> campusSchedules, List<ScheduleInfoSummary> branchSchedules, List<ScheduleInfoSummary> centerSchedules) {
        return ScheduleInfoSummaryLists.builder()
                .campusSchedules(campusSchedules)
                .branchSchedules(branchSchedules)
                .centerSchedules(centerSchedules)
                .build();
    }


    public Schedule createScheduleToSchedule(Member member, CreateSchedule request) {
        return Schedule.builder()
                .title(request.getTitle())
                .writer(member)
                .content(request.getContent())
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .semesterPermission(request.getSemesterPermission())
                .hostType(request.getHostType())
                .placeSetting(request.getPlaceSetting())
                .build();
    }

    public Schedule updateScheduleToSchedule(UpdateSchedule request, Schedule schedule) {
        schedule.setTitle(request.getTitle());
        schedule.setContent(request.getContent());
        schedule.setStartDateTime(request.getStartDateTime());
        schedule.setEndDateTime(request.getEndDateTime());
        schedule.setSemesterPermission(request.getSemesterPermission());
        schedule.setHostType(request.getHostType());
        schedule.setPlaceSetting(request.getPlaceSetting());
        return schedule;
    }

    public ScheduleId UUIDtoScheduleId(UUID scheduleId) {
        return ScheduleId.builder()
                .scheduleId(scheduleId)
                .build();
    }




}
