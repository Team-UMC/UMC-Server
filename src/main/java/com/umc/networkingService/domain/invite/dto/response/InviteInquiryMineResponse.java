package com.umc.networkingService.domain.invite.dto.response;

import com.umc.networkingService.global.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InviteInquiryMineResponse {
    private String inviteCode;
    private Role role;
    private LocalDateTime createdAt;
}
