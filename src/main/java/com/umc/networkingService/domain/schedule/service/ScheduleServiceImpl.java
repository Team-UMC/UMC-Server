package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleDetail;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummary;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryLists;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import com.umc.networkingService.domain.schedule.mapper.ScheduleMapper;
import com.umc.networkingService.domain.schedule.repository.ScheduleRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public ScheduleInfoSummariesInCalendar getCalendarByMonth(LocalDate date) {

        return scheduleMapper.toScheduleInfoSummariesInCalendar(
                scheduleRepository.findSchedulesByYearAndMonth(date).stream()
                .map(schedule -> scheduleMapper.toScheduleInfoSummaryInCalendar(schedule))
                .toList());
    }

    @Override
    public ScheduleInfoSummaryLists getScheduleLists(LocalDate date) {

        List<Schedule> schedulesLists = scheduleRepository.findSchedulesByYearAndMonth(date);

        List<ScheduleInfoSummary> campusSchedules = filterSchedulesByHostType(schedulesLists, HostType.CAMPUS);

        List<ScheduleInfoSummary> branchSchedules = filterSchedulesByHostType(schedulesLists, HostType.BRANCH);

        List<ScheduleInfoSummary> centerSchedules = filterSchedulesByHostType(schedulesLists, HostType.CENTER);

        return scheduleMapper.toScheduleInfoSummaries(campusSchedules, branchSchedules, centerSchedules);
    }

    private List<ScheduleInfoSummary> filterSchedulesByHostType(List<Schedule> schedules, HostType hostType) {
        return schedules.stream()
                .filter(schedule -> schedule.getHostType().equals(hostType))
                .map(schedule -> scheduleMapper.toScheduleInfoSummary(schedule))
                .toList();
    }

    @Override
    public ScheduleDetail getScheduleDetail(Member member, UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_SCHEDULE));

        return scheduleMapper.toScheduleDetail(schedule);
    }

    @Override
    public ScheduleId createSchedule(Member member, CreateSchedule request) {
        Schedule schedule = scheduleMapper.createScheduleToSchedule(member, request);

        scheduleRepository.save(schedule);

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }

    @Override
    @Transactional
    public ScheduleId updateSchedule(Member member, UUID scheduleId, UpdateSchedule request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_SCHEDULE));
        // 만약 수정하려는 멤버와 일정 작성자가 일치하지 않을 경우 에러 반환
        if (!schedule.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(ErrorCode.NO_PERMISSION_MEMBER);
        }

        schedule.updateSchedule(request);

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }

    @Override
    @Transactional
    public ScheduleId deleteSchedule(Member member, UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_SCHEDULE));
        // 만약 삭제하려는 멤버와 일정 작성자가 일치하지 않을 경우 에러 반환
        if (!schedule.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(ErrorCode.NO_PERMISSION_MEMBER);
        }
        schedule.delete();

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }
}
