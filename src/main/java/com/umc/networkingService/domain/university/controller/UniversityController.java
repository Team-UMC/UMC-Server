package com.umc.networkingService.domain.university.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.domain.university.service.UniversityServiceImpl;
import com.umc.networkingService.domain.university.validation.annotation.ExistUniversity;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "대학교 API", description = "일반 유저용 멤버 관련 API")
@RestController
@Validated
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityServiceImpl universityService;

    @Operation(summary = "전체 학교 조회 API",description = "전체 학교 조회 API")
    @GetMapping("")
    public BaseResponse<List<UniversityResponse.UniversityList>>
    joinUniversityList(
            @CurrentMember Member member //학교 많으면 추후에 페이징 처리하기
    ){
        return BaseResponse.onSuccess(universityService.joinUniversityList());
    }

    @Operation(summary = "학교별 세부 정보 조회 API",description = "우리 학교 정보 조회 API")
    @GetMapping("/details")
    public BaseResponse<UniversityResponse.joinUniversityDetail>
    joinUniversityDetail(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(universityService.joinUniversityDetail(member));
    }

    @Operation(summary = "전체 학교 랭킹 조회 API",description = "전체 학교 랭킹 조회 API")
    @GetMapping("/ranks")
    public BaseResponse<>
    joinUniversityRanking( ){
    }

    @Operation(summary = "학교 전체 기여도 조회 API",description = "학교 전체 기여도 조회 API")
    @GetMapping("/members")
    public BaseResponse<>
    joinUniversityContribution( ){
    }

    @Operation(summary = "학교 마스코트 조회 API",description = "학교 마스코트 조회 API")
    @GetMapping("/mascot")
    public BaseResponse<>
    joinUniversityMascot( ){
    }

    @Operation(summary = "학교 마스코트 먹이주기 API",description = "학교 마스코트 먹이주기 API")
    @PostMapping("/mascot")
    public BaseResponse<>
    join( ){
    }

    @Operation(summary = "학교 생성하기 API",description = "학교 생성하기 API")
    @PostMapping("")
    public BaseResponse<>
    feedUniversityMascot( ){
    }

    @Operation(summary = "학교 삭제하기 API",description = "학교 삭제하기 API")
    @DeleteMapping("")
    public BaseResponse<>
    deleteUniversity( ){
    }

    @Operation(summary = "학교 정보 수정하기 API",description = "학교 정보 수정하기 API")
    @PatchMapping("")
    public BaseResponse<>
    patchUniversity( ){
    }

}

