package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberSignUpResponse;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "멤버 API", description = "멤버 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    @Operation(summary = "회원가입 API", description = "최초 멤버 정보를 등록하는 API입니다.")
    @PostMapping
    public BaseResponse<MemberSignUpResponse> signUp(@Valid @RequestBody MemberSignUpRequest request) {
        return null;
    }
}
