package com.umc.networkingService.domain.schedule.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.CreateSchedule;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleDetail;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleId;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummariesInCalendar;
import com.umc.networkingService.domain.schedule.dto.response.ScheduleResponse.ScheduleInfoSummaryLists;
import com.umc.networkingService.domain.schedule.service.ScheduleService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "일정 API", description = "일정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // GET
    @Operation(summary = "일정 조회(Web) - 중앙, 지부, 학교", description = "홈화면의 일정을 조회하는 API입니다.")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public BaseResponse<ScheduleInfoSummaryLists> getScheduleLists(@RequestParam LocalDate date) {

        return BaseResponse.onSuccess((scheduleService.getScheduleLists(date)));
    }

    @Operation(summary = "일정 조회(상세조회)", description = "홈화면의 달력에서 일정을 상세조회하는 API입니다.")
    @GetMapping("/detail/{scheduleId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않은 스케줄입니다.")
    })
    public BaseResponse<ScheduleDetail> getScheduleDetail(@CurrentMember Member member,
                                                          @PathVariable("scheduleId") UUID scheduleId) {

        return BaseResponse.onSuccess(scheduleService.getScheduleDetail(member, scheduleId));
    }

    // POST

    @Operation(summary = "캘린더 조회 API", description = "홈 화면의 달력을 조회하는 API입니다.")
    @PostMapping("/calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public BaseResponse<ScheduleInfoSummariesInCalendar> getSchedule(@RequestParam LocalDate date) {

        return BaseResponse.onSuccess(scheduleService.getCalendarByMonth(date));
    }


}
