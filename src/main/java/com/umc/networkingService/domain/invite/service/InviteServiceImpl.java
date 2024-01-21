package com.umc.networkingService.domain.invite.service;

import com.umc.networkingService.domain.invite.dto.response.InviteAuthenticateResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteCreateResponse;
import com.umc.networkingService.domain.invite.dto.response.InviteInquiryMineResponse;
import com.umc.networkingService.domain.invite.entity.Invite;
import com.umc.networkingService.domain.invite.mapper.InviteMapper;
import com.umc.networkingService.domain.invite.repository.InviteRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final InviteMapper inviteMapper;

    private final InviteRepository inviteRepository;
    private final MemberService memberService;

    @Override
    public InviteCreateResponse createInviteCode(Member member, Role role) {
        checkRolePriority(member, role);
        deleteExistingInvite(member, role);
        String inviteCode = generateUniqueInviteCode();
        Invite savedInvite = saveInvite(member, role, inviteCode);
        return new InviteCreateResponse(savedInvite.getCode(), savedInvite.getRole());
    }

    @Override
    @Transactional
    public InviteAuthenticateResponse authenticateInviteCode(Member member, String inviteCode) {
        Invite savedInvite = inviteRepository.findByCode(inviteCode)
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_INVITE_CODE));

        if (savedInvite.isExpired()) {
            savedInvite.delete();
            throw new RestApiException(ErrorCode.EXPIRED_INVITE_CODE);
        }

        member.updateRole(savedInvite.getRole());
        Member savedMember = memberService.saveEntity(member);

        return new InviteAuthenticateResponse(savedMember.getRole());
    }

    @Override
    public List<InviteInquiryMineResponse> inquiryMyInviteCode(Member member) {
        List<Invite> savedInvites = inviteRepository.findAllByMember(member);

        return savedInvites.stream()
                .map(inviteMapper::toInquiryMineResponse)
                .toList();
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
