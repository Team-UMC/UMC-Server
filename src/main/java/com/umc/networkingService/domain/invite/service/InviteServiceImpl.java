package com.umc.networkingService.domain.invite.service;

import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.entity.Invite;
import com.umc.networkingService.domain.invite.repository.InviteRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;

    @Override
    public InviteCreateResponse createInviteCode(Member member, Role role) {
        checkRolePriority(member, role);
        deleteExistingInvite(member, role);
        String inviteCode = generateUniqueInviteCode();
        Invite savedInvite = saveInvite(member, role, inviteCode);
        return new InviteCreateResponse(savedInvite.getCode(), savedInvite.getRole());
    }

    private void checkRolePriority(Member member, Role role) {
        if (member.getRole().getPriority() >= role.getPriority()) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CREATE_INVITE);
        }
    }

    private void deleteExistingInvite(Member member, Role role) {
        Optional<Invite> oldInvite = inviteRepository.findByMemberAndRole(member, role);
        oldInvite.ifPresent(BaseEntity::delete);
    }

    private String generateUniqueInviteCode() {
        String inviteCode;
        do {
            inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        } while (inviteRepository.existsByCode(inviteCode));
        return inviteCode;
    }

    private Invite saveInvite(Member member, Role role, String inviteCode) {
        return inviteRepository.save(
                Invite.builder()
                        .member(member)
                        .code(inviteCode)
                        .role(role)
                        .build()
        );
    }

}
