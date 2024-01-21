package com.umc.networkingService.domain.invite.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.invite.dto.response.InviteAuthenticationResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.service.InviteService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "초대 API", description = "초대 관련 API")
@RestController
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @Operation(summary = "초대 코드 발급 API", description = "일반 부원용 또는 운영진용 초대 코드 발급 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "INVITE002", description = "상위의 역할에 대해서 초대 코드를 발급할 경우 발생")
    })
    @Parameter(name = "role", description = "초대 코드에 부여할 역할을 지정합니다.(ex. MEMBER, STAFF, CAMPUS_STAFF ...)")
    @PostMapping("/staff/invites")
    public BaseResponse<InviteCreateResponse> createInviteCode(@CurrentMember Member member,
                                                               @RequestParam Role role) {
        return BaseResponse.onSuccess(inviteService.createInviteCode(member, role));
    }

    @Operation(summary = "초대 코드 확인 API", description = "발급 받은 초대 코드를 검증하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "INVITE001", description = "만료된 초대 코드일 경우 발생")
    })
    @Parameter(name = "inviteCode", in = ParameterIn.PATH, description = "발급 받은 초대 코드를 입력하는 파라미터입니다.")
    @PostMapping("/invites/{inviteCode}")
    public BaseResponse<InviteAuthenticationResponse> authenticationInviteCode(@CurrentMember Member member,
                                                                               @PathVariable String inviteCode) {
        return BaseResponse.onSuccess(inviteService.authenticationInviteCode(member, inviteCode));
    }
}
