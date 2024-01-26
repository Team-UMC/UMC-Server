package com.umc.networkingService.domain.schedule.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.service.ScheduleService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "운영진 달력 API", description = "운영진 달력 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff/schedules")
public class StaffScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "일정 추가 API", description = "운영진 관리 페이지의 일정을 추가하는 API입니다.")
    @PostMapping
    public BaseResponse<ScheduleId> createSchedule(@CurrentMember Member member,
                                                   @RequestBody CreateSchedule request) {

        return BaseResponse.onSuccess(scheduleService.createSchedule(member, request));
    }

    @Operation(summary = "일정 수정 API", description = "운영진 관리 페이지의 일정을 수정하는 API입니다.")
    @PostMapping("/update/{scheduleId}")
    public BaseResponse<ScheduleId> updateSchedule(@CurrentMember Member member,
                                                   @PathVariable("scheduleId") UUID scheduleId,
                                                   @RequestBody UpdateSchedule request) {

        return BaseResponse.onSuccess(scheduleService.updateSchedule(member, scheduleId, request));
    }

    // DELETE

    @Operation(summary = "일정 삭제 API", description = "운영진 관리 페이지의 일정을 삭제하는 API입니다.")
    @DeleteMapping("/{scheduleId}")
    public BaseResponse<ScheduleId> deleteSchedule(@PathVariable("scheduleId") UUID scheduleId) {

        return BaseResponse.onSuccess(scheduleService.deleteSchedule(scheduleId));
    }

}
