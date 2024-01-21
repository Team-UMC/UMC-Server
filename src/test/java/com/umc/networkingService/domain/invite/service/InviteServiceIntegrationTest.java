package com.umc.networkingService.domain.invite.service;

import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.entity.Invite;
import com.umc.networkingService.domain.invite.repository.InviteRepository;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.support.ServiceIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Invite 서비스의 ")
public class InviteServiceIntegrationTest extends ServiceIntegrationTestConfig {

    @Autowired InviteService inviteService;
    @Autowired InviteRepository inviteRepository;

    @Test
    @DisplayName("초대 코드 발급 테스트")
    @Transactional
    public void createInviteCode() {
        // given
        member.updateRole(Role.CAMPUS_STAFF);

        // when
        InviteCreateResponse response = inviteService.createInviteCode(member, Role.MEMBER);

        // then
        Optional<Invite> optionalInvite = inviteRepository.findByMemberAndRole(member, Role.MEMBER);
        assertTrue(optionalInvite.isPresent());
        Invite savedInvite = optionalInvite.get();

        assertEquals(response.getInviteCode(), savedInvite.getCode());
        assertEquals(response.getRole(), savedInvite.getRole());
    }

    @Test
    @DisplayName("초대 코드 발급 테스트 - 상위 역할 부여 시")
    @Transactional
    public void createInviteCodeWithTopRole() {
        // given
        member.updateRole(Role.CAMPUS_STAFF);

        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> inviteService.createInviteCode(member, Role.CENTER_STAFF));

        // then
        assertEquals(ErrorCode.UNAUTHORIZED_CREATE_INVITE, exception.getErrorCode());

        Optional<Invite> optionalInvite = inviteRepository.findByMemberAndRole(member, Role.CENTER_STAFF);
        assertFalse(optionalInvite.isPresent());
    }
}
