package com.umc.networkingService.domain.member.controller;


import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.dto.response.MemberSignUpResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "소셜 로그인", description = "네이버, 카카오, 구글, 애플 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "COMMON500", description = "소셜 서버와의 통신 에러" , content =
            @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/login")
    public BaseResponse<MemberLoginResponse> socialLogin(@RequestParam(value = "accessToken") String accessToken,
                                                         @RequestParam(value = "socialType") SocialType socialType) {
        return BaseResponse.onSuccess(memberService.socialLogin(accessToken, socialType));

    }

    @Operation(summary = "회원가입 API", description = "최초 멤버 정보를 등록하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "UNIVERSITY001", description = "대학교명을 잘못 입력하였을 경우 발생"),
            @ApiResponse(responseCode = "BRANCH001", description = "대학교가 지부랑 연결되어 있지 않을 경우 발생")
    })
    @PostMapping
    public BaseResponse<MemberSignUpResponse> signUp(@CurrentMember Member member,
                                                     @Valid @RequestBody MemberSignUpRequest request) {
        return BaseResponse.onSuccess(memberService.signUp(member, request));
    }
}
