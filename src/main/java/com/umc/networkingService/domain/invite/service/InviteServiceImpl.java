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

    // 초대 코드 생성 함수
    @Override
    public InviteCreateResponse createInviteCode(Member member, Role role) {
        checkRolePriority(member, role);
        deleteExistingInvite(member, role);
        String inviteCode = generateUniqueInviteCode();
        Invite savedInvite = saveInvite(member, role, inviteCode);
        return new InviteCreateResponse(savedInvite.getCode(), savedInvite.getRole());
    }

    // 초대 코드 인증 함수
    @Override
    @Transactional
    public InviteAuthenticateResponse authenticateInviteCode(Member member, String inviteCode) {
        // 존재하지 않는 초대 코드인 경우 예외 처리
        Invite savedInvite = inviteRepository.findByCode(inviteCode)
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_INVITE_CODE));

        // 만료된 초대 코드인 경우 삭제 후 예외 처리
        if (savedInvite.isExpired()) {
            savedInvite.delete();
            throw new RestApiException(ErrorCode.EXPIRED_INVITE_CODE);
        }

        // 초대 코드에 부여된 역할 부여
        member.updateRole(savedInvite.getRole());
        Member savedMember = memberService.saveEntity(member);

        return new InviteAuthenticateResponse(savedMember.getRole());
    }

    // 나의 초대 코드 조회 함수
    @Override
    public InviteInquiryMineResponse inquiryMyInviteCode(Member member) {
        // 본인이 생성한 초대 코드 조회
        List<Invite> savedInvites = inviteRepository.findAllByMember(member);

        List<InviteInquiryMineResponse.InviteInfo> invites = savedInvites.stream()
                .map(inviteMapper::toInquiryMineResponse)
                .toList();

        return new InviteInquiryMineResponse(invites);
    }

    // 본인의 역할 이상의 역할 부여를 확인하는 함수
    private void checkRolePriority(Member member, Role role) {
        if (member.getRole().getPriority() >= role.getPriority()) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CREATE_INVITE);
        }
    }

    // 존재하는 초대 코드 삭제 함수
    private void deleteExistingInvite(Member member, Role role) {
        Optional<Invite> oldInvite = inviteRepository.findByMemberAndRole(member, role);
        oldInvite.ifPresent(BaseEntity::delete);
    }

    // 중복되지 않는 초대 코드 생성 함수
    private String generateUniqueInviteCode() {
        String inviteCode;
        do {
            inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        } while (inviteRepository.existsByCode(inviteCode));
        return inviteCode;
    }

    // 초대 코드 저장 함수
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
