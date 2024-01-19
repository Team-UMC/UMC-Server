package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateProfileRequest;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "멤버 API", description = "운영진용 멤버 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff/members")
public class StaffMemberController {
    private final MemberService memberService;
    @Operation(summary = "유저 정보 수정 API", description = "운영진이 특정 유저의 직책, 기수, 파트를 수정하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PostMapping("/{memberId}/update")
    public BaseResponse<MemberIdResponse> updateProfile(@CurrentMember Member member,
                                                        @PathVariable(value = "memberId") UUID memberId,
                                                        @RequestBody MemberUpdateProfileRequest request) {
        return BaseResponse.onSuccess(memberService.updateProfile(member, memberId));
    }
}
