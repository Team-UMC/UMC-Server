package com.umc.networkingService.domain.schedule.mapper;

import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfo;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoList;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import java.util.List;
import java.util.UUID;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleInfo toScheduleInfo(Schedule schedule) {
        return ScheduleInfo.builder()
                .scheduleId(schedule.getId())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .hostType(schedule.getHostType().toString())
                .build();
    }

    public ScheduleInfoList toScheduleInfoList(List<ScheduleInfo> schedules) {
        return ScheduleInfoList.builder()
                .schedules(schedules)
                .build();
    }

    public Schedule createScheduleToSchedule(CreateSchedule request) {
        return Schedule.builder()
                .title(request.getTitle())
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
