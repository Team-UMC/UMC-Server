package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfo;
import com.umc.networkingService.domain.schedule.mapper.ScheduleMapper;
import com.umc.networkingService.domain.schedule.repository.ScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleInfo> getSchedulesByMonth(Long month) {


        return scheduleRepository.findSchedulesByMonth(month).stream()
                .map(schedule -> scheduleMapper.toScheduleInfo(schedule))
                .toList();
    }
}
