package com.umc.networkingService.domain.invite.dto.response;

import com.umc.networkingService.global.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteAuthenticationResponse {
    private Role role;
}
