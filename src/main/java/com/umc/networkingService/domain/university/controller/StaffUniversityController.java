package com.umc.networkingService.domain.university.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.university.dto.request.UniversityRequest;
import com.umc.networkingService.domain.university.dto.response.UniversityResponse;
import com.umc.networkingService.domain.university.service.UniversityServiceImpl;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Tag(name = "운영진 대학교 API", description = "운영진용 대학교 관련 API")
@RestController
@Validated
@RequestMapping("/staff/universities")
@RequiredArgsConstructor
public class StaffUniversityController {

    private final UniversityServiceImpl universityService;

    @Operation(summary = "학교 생성하기 API",description = "학교 생성하기 API")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<UniversityResponse.UniversityId>
    createUniversity(
            @Valid @RequestPart("request") UniversityRequest.universityInfo request,
            @RequestPart(name = "file", required = false) MultipartFile universityLogo,
            @RequestPart(name = "file", required = false) MultipartFile semesterLogo
    )
    {
        return BaseResponse.onSuccess(universityService.createUniversity(request, universityLogo, semesterLogo));
    }

    @Operation(summary = "학교 삭제하기 API",description = "학교 삭제하기 API")
    @DeleteMapping("/{universityId}")
    public BaseResponse<UniversityResponse.UniversityId>
    deleteUniversity(
            @PathVariable @Valid UUID universityId
    ){
        return BaseResponse.onSuccess(universityService.deleteUniversity(universityId));
    }

    @Operation(summary = "학교 정보 수정하기 API",description = "학교 정보 수정하기 API")
    @PatchMapping(value = "/{universityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<UniversityResponse.UniversityId>
    patchUniversity(
            @PathVariable @Valid UUID universityId,
            @Valid @RequestPart("request") UniversityRequest.universityInfo request,
            @RequestPart(name = "file", required = false) MultipartFile universityLogo,
            @RequestPart(name = "file", required = false) MultipartFile semesterLogo

    ){
        return BaseResponse.onSuccess(universityService.patchUniversity(request, universityId, universityLogo, semesterLogo));
    }

}


