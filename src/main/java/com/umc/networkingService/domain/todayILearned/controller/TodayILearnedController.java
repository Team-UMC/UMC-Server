package com.umc.networkingService.domain.todayILearned.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedCreate;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedUpdate;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedId;
import com.umc.networkingService.domain.todayILearned.dto.response.TodayILearnedResponse.TodayILearnedInfos;
import com.umc.networkingService.domain.todayILearned.service.TodayILearnedService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Today I Learned API", description = "Today I Learned 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/today-i-learned")
public class TodayILearnedController {

    private final TodayILearnedService todayILearnedService;


    @Operation(summary = "Today I Learned 추가", description = "TIL을 추가하는 API입니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public BaseResponse<TodayILearnedResponse.TodayILearnedCreate> createTodayILearned(@CurrentMember Member member,
                                                                                       @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                                                       @RequestPart("request") TodayILearnedCreate request) {

        return BaseResponse.onSuccess(todayILearnedService.createTodayILearned(member, files, request));
    }

    @Operation(summary = "Today I Learned 수정", description = "TIL를 수정하는 API입니다.")
    @PostMapping(value = {"update/{todayILearnedId}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public BaseResponse<TodayILearnedId> updateTodayILearned(@CurrentMember Member member,
                                                             @PathVariable("todayILearnedId") UUID todayILearnedId,
                                                             @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                             @RequestPart("request") TodayILearnedUpdate request) {

        return BaseResponse.onSuccess(todayILearnedService.updateTodayILearned(member, todayILearnedId, files, request));
    }


    @Operation(summary = "Today I Learned 조회(일별)", description = "TIL(일별)을 조회하는 API입니다.")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name="date", description = "현재 날짜를 2024-02-08 형식으로 전달해 주세요.")
    })
    public BaseResponse<TodayILearnedInfos> getTodayILearnedInfos(@CurrentMember Member member,
                                                                  @RequestParam(value = "date") String date) {

        return BaseResponse.onSuccess(todayILearnedService.getTodayILearnedInfos(member, date));
    }


    @Operation(summary = "Today I Learned 삭제", description = "TIL을 삭제하는 API입니다.")
    @DeleteMapping("/{todayILearnedId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public BaseResponse<TodayILearnedId> deleteTodayILearned(@CurrentMember Member member,
                                                             @PathVariable("todayILearnedId") UUID todayILearnedId) {
        return BaseResponse.onSuccess(todayILearnedService.deleteTodayILearned(member, todayILearnedId));
    }
}
