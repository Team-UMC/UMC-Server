package com.umc.networkingService.domain.schedule.mapper;

import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfo;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoList;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleInfo toScheduleInfo(Schedule schedule) {
        return ScheduleInfo.builder()
                .id(schedule.getId())
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

    public Schedule toSchedule(CreateSchedule request) {
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




}
