package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateNewAccessTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "멤버 API", description = "멤버 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    @Operation(summary = "회원가입 API", description = "최초 멤버 정보를 등록하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "UNIVERSITY001", description = "대학교명을 잘못 입력하였을 경우 발생"),
            @ApiResponse(responseCode = "BRANCH001", description = "대학교가 지부랑 연결되어 있지 않을 경우 발생")
    })
    @PostMapping
    public BaseResponse<MemberIdResponse> signUp(@CurrentMember Member member,
                                                 @Valid @RequestBody MemberSignUpRequest request) {
        return BaseResponse.onSuccess(memberService.signUp(member, request));
    }

    @Operation(summary = "accessToken 재발급 API", description = "refreshToken가 유효하다면 새로운 accessToken을 발급하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH006", description = "유효하지 않는 RefreshToken일 경우 발생")
    })
    @GetMapping("/token/refresh")
    public BaseResponse<MemberGenerateNewAccessTokenResponse> regenerateToken(@CurrentMember Member member,
                                                                              @RequestHeader(value = "refreshToken") String refreshToken) {
        return BaseResponse.onSuccess(memberService.generateNewAccessToken(refreshToken, member));
    }

    @Operation(summary = "로그아웃 API", description = "해당 유저의 refreshToken을 삭제하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @DeleteMapping("/logout")
    public BaseResponse<MemberIdResponse> logout(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.logout(member));
    }

    @Operation(summary = "회원 탈퇴 API", description = "해당 유저 정보를 삭제하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @DeleteMapping
    public BaseResponse<MemberIdResponse> withdrawal(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.withdrawal(member));
    }
}
