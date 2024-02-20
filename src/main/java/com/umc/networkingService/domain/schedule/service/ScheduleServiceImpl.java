package com.umc.networkingService.domain.schedule.service;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.*;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import com.umc.networkingService.domain.schedule.mapper.ScheduleMapper;
import com.umc.networkingService.domain.schedule.repository.ScheduleRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.MemberErrorCode;
import com.umc.networkingService.global.common.exception.code.ScheduleErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    private final MemberService memberService;

    @Override
    public ScheduleInfoSummariesInCalendar getScheduleByMonth(Member loginMember, LocalDate date) {

        Member member = memberService.loadEntity(loginMember.getId());

        List<Schedule> schedulesLists = validateSchedules(member, scheduleRepository.findSchedulesByYearAndMonth(date))
                .stream().sorted(Comparator.comparing(Schedule::getStartDateTime)).toList();

        return scheduleMapper.toScheduleInfoSummariesInCalendar(
                schedulesLists.stream()
                        .map(scheduleMapper::toScheduleInfoSummaryInCalendar)
                        .toList());
    }

    @Override
    public ScheduleInfos getScheduleByMonthToWeb(Member loginMember, LocalDate date) {
        Member member = memberService.loadEntity(loginMember.getId());

        List<Schedule> schedulesLists = validateSchedules(member, scheduleRepository.findSchedulesByYearAndMonth(date))
                .stream().sorted(Comparator.comparing(Schedule::getStartDateTime)).toList();

        return new ScheduleInfos(
                schedulesLists.stream()
                        .map(scheduleMapper::toScheduleInfo)
                        .toList());
    }

    @Override
    public ScheduleInfoSummaryLists getScheduleLists(Member loginMember, LocalDate date) {

        Member member = memberService.loadEntity(loginMember.getId());

        List<Schedule> schedulesLists = validateSchedules(member,
                scheduleRepository.findSchedulesByYearAndMonth(date));

        List<ScheduleInfoSummary> campusSchedules = filterSchedulesByHostType(schedulesLists, HostType.CAMPUS);

        List<ScheduleInfoSummary> branchSchedules = filterSchedulesByHostType(schedulesLists, HostType.BRANCH);

        List<ScheduleInfoSummary> centerSchedules = filterSchedulesByHostType(schedulesLists, HostType.CENTER);

        return scheduleMapper.toScheduleInfoSummaries(campusSchedules, branchSchedules, centerSchedules);
    }

    private List<ScheduleInfoSummary> filterSchedulesByHostType(List<Schedule> schedules, HostType hostType) {
        return schedules.stream()
                .filter(schedule -> schedule.getHostType().equals(hostType))
                .map(scheduleMapper::toScheduleInfoSummary)
                .sorted(Comparator.comparing(ScheduleInfoSummary::getStartDateTime))
                .toList();
    }

    private List<Schedule> validateSchedules(Member member, List<Schedule> schedules) {
        return schedules.stream()
                .filter(schedule -> validateScheduleUniversityAndBranch(member, schedule))
                .filter(schedule -> validateScheduleSemester(member, schedule))
                .toList();
    }

    private boolean validateScheduleUniversityAndBranch(Member member, Schedule schedule) {
        if (schedule.getHostType().equals(HostType.CENTER))
            return true;
        if (schedule.getHostType().equals(HostType.BRANCH)
                && member.getBranch() == schedule.getWriter().getBranch())
            return true;
        return schedule.getHostType().equals(HostType.CAMPUS)
                && member.getUniversity() == schedule.getWriter().getUniversity();
    }

    private boolean validateScheduleSemester(Member member, Schedule schedule) {
        return member.getSemesters().stream()
                .anyMatch(memberSemester -> schedule.getSemesterPermission().contains(memberSemester));
    }

    @Override
    public ScheduleInfo getScheduleDetail(Member member, UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RestApiException(ScheduleErrorCode.EMPTY_SCHEDULE));

        return scheduleMapper.toScheduleInfo(schedule);
    }

    @Override
    public boolean existsByHostType(HostType hostType) {
        return scheduleRepository.existsByHostType(hostType);
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
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ScheduleErrorCode.EMPTY_SCHEDULE));
        // 만약 수정하려는 멤버와 일정 작성자가 일치하지 않을 경우 에러 반환
        if (!schedule.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(MemberErrorCode.NO_PERMISSION_MEMBER);
        }

        schedule.updateSchedule(request);

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }

    @Override
    @Transactional
    public ScheduleId deleteSchedule(Member member, UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RestApiException(ScheduleErrorCode.EMPTY_SCHEDULE));
        // 만약 삭제하려는 멤버와 일정 작성자가 일치하지 않을 경우 에러 반환
        if (!schedule.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(MemberErrorCode.NO_PERMISSION_MEMBER);
        }
        schedule.delete();

        return scheduleMapper.UUIDtoScheduleId(schedule.getId());
    }
}