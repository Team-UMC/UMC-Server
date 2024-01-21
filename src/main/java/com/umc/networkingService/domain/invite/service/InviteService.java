package com.umc.networkingService.domain.invite.service;

import com.umc.networkingService.domain.invite.dto.response.InviteAuthenticateResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;

public interface InviteService {
    InviteCreateResponse createInviteCode(Member member, Role role);
    InviteAuthenticateResponse authenticateInviteCode(Member member, String inviteCode);
}
