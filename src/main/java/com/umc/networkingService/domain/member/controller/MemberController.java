package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.test.dto.TestResponse;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login", description = "네이버, 카카오, 구글, 애플 로그인")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "소셜 로그인", description = "네이버, 카카오, 구글, 애플 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "COMMON500", description = "소셜 서버와의 통신 에러" , content =
            @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public BaseResponse<MemberLoginResponse> socialLogin(@RequestParam(value = "accessToken") String accessToken,
                                           @RequestParam(value = "socialType") SocialType socialType) {
        return BaseResponse.onSuccess(memberService.socialLogin(accessToken, socialType));
    }
}
