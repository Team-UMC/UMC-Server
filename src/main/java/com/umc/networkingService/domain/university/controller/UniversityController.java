package com.umc.networkingService.domain.university.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.PointType;
import com.umc.networkingService.domain.university.dto.request.UniversityRequest;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.service.UniversityServiceImpl;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "대학교 API", description = "일반 유저용 대학교 관련 API")
@RestController
@Validated
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityServiceImpl universityService;

    @Operation(summary = "전체 학교 조회 API",description = "전체 학교 조회 API")
    @GetMapping("")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<UniversityResponse.JoinUniversities>
    joinUniversityList(
            @CurrentMember Member member //학교 많으면 추후에 페이징 처리하기
    ){
        return BaseResponse.onSuccess(universityService.joinUniversityList());
    }

    @Operation(summary = "우리 학교 세부 정보 조회 API",description = "우리 학교 정보 조회 API")
    @GetMapping("/details")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<UniversityResponse.joinUniversityDetail>
    joinUniversityDetail(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(universityService.joinUniversityDetail(member));
    }

    @Operation(summary = "전체 학교 랭킹 조회 API",description = "전체 학교 랭킹 조회 API")
    @GetMapping("/ranks")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<UniversityResponse.JoinUniversityRanks>
    joinUniversityRanking(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(universityService.joinUniversityRankingList(member));
    }

    @Operation(summary = "우리 학교 전체 기여도 랭킹 조회 API",description = "학교 전체 기여도 조회 API")
    @GetMapping("/members")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<UniversityResponse.JoinContributionRanks>
    joinUniversityContribution(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(universityService.joinContributionRankingList(member));
    }

    @Operation(summary = "우리 학교 마스코트 조회 API",description = "학교 마스코트 조회 API")
    @GetMapping("/mascot")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BRANCH001", description = "지부가 존재하지 않음")
    })
    public BaseResponse<UniversityResponse.joinUniversityMascot>
    joinUniversityMascot(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(universityService.joinUniversityMascot(member));
    }

    @Operation(summary = "우리 학교 마스코트 먹이주기 API",description = "학교 마스코트 먹이주기 API")
    @PostMapping("/mascot")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "MEMBER008", description = "마스코트에 줄 포인트가 충분하지 않음")
    })
    public BaseResponse<Long>
    postMascotPoint(
            @CurrentMember Member member,
            @RequestBody @Valid PointType pointType
            ){
        return BaseResponse.onSuccess(universityService.feedUniversityMascot(member, pointType)); //현재 대학 포인트
    }

}

