package com.umc.networkingService.domain.invite.service;

import com.umc.networkingService.domain.invite.dto.response.InviteAuthenticationResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;

public interface InviteService {
    InviteCreateResponse createInviteCode(Member member, Role role);
    InviteAuthenticationResponse authenticationInviteCode(Member member, String inviteCode);
}
