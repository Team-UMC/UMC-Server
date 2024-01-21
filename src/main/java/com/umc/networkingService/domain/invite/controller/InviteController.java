package com.umc.networkingService.domain.invite.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.service.InviteService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameter(name = "role", description = "초대 코드에 부여할 역할을 지정합니다.(ex. MEMBER, STAFF, CAMPUS_STAFF ...)")
    @PostMapping("/staff/invites")
    public BaseResponse<InviteCreateResponse> createInviteCode(@CurrentMember Member member,
                                                               @RequestParam Role role) {
        return BaseResponse.onSuccess(inviteService.createInviteCode(member, role));
    }

}
