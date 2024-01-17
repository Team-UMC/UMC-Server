package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberSignUpResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Role;
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

    private final MemberService memberService;
    @Operation(summary = "회원가입 API", description = "최초 멤버 정보를 등록하는 API입니다.")
    @PostMapping
    public BaseResponse<MemberSignUpResponse> signUp(@CurrentMember Member member,
                                                     @Valid @RequestBody MemberSignUpRequest request) {
        return BaseResponse.onSuccess(memberService.signUp(member, request));
    }
}
