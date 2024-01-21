package com.umc.networkingService.domain.invite.service;

import com.umc.networkingService.domain.invite.dto.response.InviteAuthenticateResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.entity.Invite;
import com.umc.networkingService.domain.invite.repository.InviteRepository;
import com.umc.networkingService.domain.member.entity.Member;
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

    @Test
    @DisplayName("초대 코드 인증 테스트")
    @Transactional
    public void authenticateInviteCode() {
        // given
        String code = "inviteCode";

        Invite invite = Invite.builder()
                .member(member)
                .code(code)
                .role(Role.BRANCH_STAFF)
                .build();
        inviteRepository.save(invite);

        // when
        InviteAuthenticateResponse response = inviteService.authenticateInviteCode(member, code);

        // then
        assertEquals(Role.BRANCH_STAFF, response.getRole());
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        assertEquals(Role.BRANCH_STAFF, optionalMember.get().getRole());
    }

    @Test
    @DisplayName("초대 코드 인증 테스트 - 만료된 초대 코드 사용 시")
    @Transactional
    public void authenticateInviteCodeWithExpiredCode() {
        // given
        String code = "inviteCode";

        // when
        RestApiException exception = assertThrows(RestApiException.class,
                () -> inviteService.authenticateInviteCode(member, code));

        // then
        assertEquals(ErrorCode.EXPIRED_INVITE_CODE, exception.getErrorCode());
    }
}
