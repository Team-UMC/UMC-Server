package com.umc.networkingService.domain.university.controller;

import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.PointType;
import com.umc.networkingService.domain.university.dto.request.UniversityRequest;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.service.UniversityServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "대학교 API", description = "운영진용 멤버 관련 API")
@RestController
@Validated
@RequestMapping("/staff/universities")
@RequiredArgsConstructor
public class StaffUniversityController {

    private final UniversityServiceImpl universityService;

    @Operation(summary = "학교 생성하기 API",description = "학교 생성하기 API")
    @PostMapping("")
    public BaseResponse<UUID>
    createUniversity(
            @CurrentMember Member member,
            @RequestBody @Valid UniversityRequest.createUniversity request
    )
    {
        return BaseResponse.onSuccess(universityService.createUniversity(request));
    }

    @Operation(summary = "학교 삭제하기 API",description = "학교 삭제하기 API")
    @DeleteMapping("")
    public BaseResponse<String>
    deleteUniversity(
            @CurrentMember Member member,
            @RequestBody @Valid UUID universityId
    ){
        universityService.deleteUniversity(universityId);
        return BaseResponse.onSuccess("학교 삭제 완료");
    }

    @Operation(summary = "학교 정보 수정하기 API",description = "학교 정보 수정하기 API")
    @PatchMapping("")
    public BaseResponse<String>
    patchUniversity(
            @CurrentMember Member member,
            @RequestBody @Valid UniversityRequest.patchUniversity request
    ){
        universityService.patchUniversity(request);
        return BaseResponse.onSuccess("학교 정보 수정 완료");
    }

}


