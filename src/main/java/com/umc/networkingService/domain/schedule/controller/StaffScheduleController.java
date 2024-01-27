package com.umc.networkingService.domain.schedule.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.service.ScheduleService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "운영진 스케줄 API", description = "운영진 스케줄 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff/schedules")
public class StaffScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "일정 추가 API", description = "운영진 관리 페이지의 일정을 추가하는 API입니다.")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<ScheduleId> createSchedule(@CurrentMember Member member,
                                                   @RequestBody CreateSchedule request) {

        return BaseResponse.onSuccess(scheduleService.createSchedule(member, request));
    }

    @Operation(summary = "일정 수정 API", description = "운영진 관리 페이지의 일정을 수정하는 API입니다.")
    @PostMapping("/update/{scheduleId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "SCHEDULE001", description = "존재하지 않은 스케줄입니다.")
    })
    public BaseResponse<ScheduleId> updateSchedule(@CurrentMember Member member,
                                                   @PathVariable("scheduleId") UUID scheduleId,
                                                   @RequestBody UpdateSchedule request) {

        return BaseResponse.onSuccess(scheduleService.updateSchedule(member, scheduleId, request));
    }

    // DELETE

    @Operation(summary = "일정 삭제 API", description = "운영진 관리 페이지의 일정을 삭제하는 API입니다.")
    @DeleteMapping("/{scheduleId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "SCHEDULE001", description = "존재하지 않은 스케줄입니다.")
    })
    public BaseResponse<ScheduleId> deleteSchedule(@CurrentMember Member member,
                                                   @PathVariable("scheduleId") UUID scheduleId) {

        return BaseResponse.onSuccess(scheduleService.deleteSchedule(member, scheduleId));
    }

}
