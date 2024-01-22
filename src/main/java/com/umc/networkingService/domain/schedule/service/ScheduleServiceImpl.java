package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoList;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import com.umc.networkingService.domain.schedule.mapper.ScheduleMapper;
import com.umc.networkingService.domain.schedule.repository.ScheduleRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
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
    public ScheduleId createSchedule(CreateSchedule request) {
        Schedule schedule = scheduleMapper.createScheduleToSchedule(request);

        scheduleRepository.save(schedule);

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }

    @Override
    public ScheduleId updateSchedule(UUID scheduleId, UpdateSchedule request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_SCHEDULE));
        schedule = scheduleMapper.updateScheduleToSchedule(request, schedule);

        scheduleRepository.save(schedule);

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }

    @Override
    public ScheduleId deleteSchedule(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_SCHEDULE));

        schedule.delete();
        scheduleRepository.save(schedule);

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }


}
