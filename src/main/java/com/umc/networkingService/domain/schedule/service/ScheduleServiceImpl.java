package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfo;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoList;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import com.umc.networkingService.domain.schedule.mapper.ScheduleMapper;
import com.umc.networkingService.domain.schedule.repository.ScheduleRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public ScheduleInfoList getCalendarByMonth(Long month) {


        return scheduleMapper.toScheduleInfoList(
                scheduleRepository.findSchedulesByMonth(month).stream()
                .map(schedule -> scheduleMapper.toScheduleInfo(schedule))
                .toList());
    }

    @Override
    public UUID createSchedule(CreateSchedule request) {
        Schedule schedule = scheduleMapper.toSchedule(request);

        scheduleRepository.save(schedule);

        return schedule.getId();
    }
}
