package com.umc.networkingService.domain.invite.dto.response;

import com.umc.networkingService.global.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteCreateResponse {
    private String inviteCode;
    private Role role;
}
