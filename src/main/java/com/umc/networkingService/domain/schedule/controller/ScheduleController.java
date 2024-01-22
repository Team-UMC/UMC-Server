package com.umc.networkingService.domain.schedule.controller;

import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleDetail;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryLists;
import com.umc.networkingService.domain.schedule.service.ScheduleService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "달력 API", description = "달력 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // GET
    @Operation(summary = "일정 조회(Web) - 중앙, 지부, 학교", description = "홈화면의 일정을 조회하는 API입니다.")
    @GetMapping
    public BaseResponse<ScheduleInfoSummaryLists> getScheduleLists(@RequestParam Long month) {

        return BaseResponse.onSuccess((scheduleService.getScheduleLists(month)));
    }

    @Operation(summary = "일정 조회(상세조회)", description = "홈화면의 달력에서 일정을 상세조회하는 API입니다.")
    @GetMapping("/detail/{scheduleId}")
    public BaseResponse<ScheduleDetail> getScheduleDetail(@PathVariable("scheduleId") UUID scheduleId) {

        return BaseResponse.onSuccess(scheduleService.getScheduleDetail(scheduleId));
    }

    // POST

    @Operation(summary = "캘린더 조회 API", description = "홈 화면의 달력을 조회하는 API입니다.")
    @PostMapping("/calendar")
    public BaseResponse<ScheduleInfoSummariesInCalendar> getSchedule(@RequestParam Long month) {

        return BaseResponse.onSuccess(scheduleService.getCalendarByMonth(month));
    }

    @Operation(summary = "일정 추가 API", description = "운영진 관리 페이지의 일정을 추가하는 API입니다.")
    @PostMapping
    public BaseResponse<ScheduleId> createSchedule(@RequestBody CreateSchedule request) {

        return BaseResponse.onSuccess(scheduleService.createSchedule(request));
    }

    @Operation(summary = "일정 수정 API", description = "운영진 관리 페이지의 일정을 수정하는 API입니다.")
    @PostMapping("/update/{scheduleId}")
    public BaseResponse<ScheduleId> updateSchedule(@PathVariable("scheduleId") UUID scheduleId, @RequestBody UpdateSchedule request) {

        return BaseResponse.onSuccess(scheduleService.updateSchedule(scheduleId, request));
    }

    // DELETE

    @Operation(summary = "일정 삭제 API", description = "운영진 관리 페이지의 일정을 삭제하는 API입니다.")
    @DeleteMapping("/{scheduleId}")
    public BaseResponse<ScheduleId> deleteSchedule(@PathVariable("scheduleId") UUID scheduleId) {

        return BaseResponse.onSuccess(scheduleService.deleteSchedule(scheduleId));
    }


}
